package com.dulkirfabric.util.render

import com.dulkirfabric.DulkirModFabric.mc
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.LightTexture
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import java.awt.Color
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

object WorldRenderUtils {

    fun drawWireFrame(
        context: WorldRenderContext,
        box: AABB,
        color: Color,
        thickness: Float,
        depthTest: Boolean = true
    ) {
        val matrices = context.matrices() ?: return
        val layer = if (depthTest) DulkirRenderTypes.DULKIR_QUADS else DulkirRenderTypes.DULKIR_QUADS_ESP
        val camera = context.gameRenderer().mainCamera
        val camPos = camera.position()
        matrices.translate(-camPos.x, -camPos.y, -camPos.z)
        val buf = RenderUtil.getBufferFor(layer)
        val me = matrices.last()

        fun line(
            x1: Number, y1: Number, z1: Number,
            x2: Number, y2: Number, z2: Number
        ) {
            line(me, buf, x1, y1, z1, x2, y2, z2, color, thickness)
        }

        matrices.pushPose()

        // bottom
        line(box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ)
        line(box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ)
        line(box.maxX, box.minY, box.maxZ, box.minX, box.minY, box.maxZ)
        line(box.minX, box.minY, box.maxZ, box.minX, box.minY, box.minZ)

        line(box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ)

        // top
        line(box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ)
        line(box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ)
        line(box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ)
        line(box.minX, box.maxY, box.maxZ, box.minX, box.maxY, box.minZ)

        // some redraws (blame strips) and getting the rest of the vertical columns
        line(box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ)
        line(box.maxX, box.maxY, box.minZ, box.maxX, box.minY, box.minZ)
        line(box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ)
        line(box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ)
        line(box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ)
        line(box.minX, box.maxY, box.maxZ, box.minX, box.minY, box.maxZ)

        layer.draw(buf.buildOrThrow())

        matrices.popPose()
    }

    /**
     * If you intend to show with distance, use a waypoint I think. If you're looking at this
     * statement and screaming at me for forgetting some use case, either let me know or compile
     * a method that accomplishes your goals based off of this example code. Neither of these
     * things should be incredibly difficult.
     */
    fun drawText(
        text: Component,
        context: WorldRenderContext,
        pos: Vec3,
        depthTest: Boolean = true, // TODO
        scale: Float = 1f
    ) {
        val layer = DulkirRenderTypes.DULKIR_QUADS_ESP

        // Minecraft vertex consumer because we still hook into their renderer and do immediate text rendering
        val vertexConsumer = context.worldRenderer().renderBuffers.bufferSource()
        val matrices = context.matrices() ?: return
        matrices.pushPose()
        val camera = context.gameRenderer().mainCamera;
        matrices.translate(
            pos.x - camera.position().x,
            pos.y - camera.position().y,
            pos.z - camera.position().z
        )
        matrices.mulPose(camera.rotation())
        matrices.scale(.025f * scale, -.025f * scale, 1F)
        val matrix4f = matrices.last().pose()
        val font = mc.font
        val j: Int = (.25 * 255.0f).toInt() shl 24
        val buf = RenderUtil.getBufferFor(layer)
        buf.addVertex(matrix4f, -1.0f - font.width(text) / 2, -1.0f, 0.0f)
            .setColor(j)
            .setLight(LightTexture.FULL_BRIGHT)
        buf.addVertex(matrix4f, -1.0f - font.width(text) / 2, font.lineHeight.toFloat(), 0.0f)
            .setColor(j)
            .setLight(LightTexture.FULL_BRIGHT)
        buf.addVertex(matrix4f, font.width(text).toFloat() / 2, font.lineHeight.toFloat(), 0.0f)
            .setColor(j)
            .setLight(LightTexture.FULL_BRIGHT)
        buf.addVertex(matrix4f, font.width(text).toFloat() / 2, -1.0f, 0.0f)
            .setColor(j)
            .setLight(LightTexture.FULL_BRIGHT)

        matrices.translate(0F, 0F, 0.01F)
        layer.draw(buf.buildOrThrow())
        font.drawInBatch(
            text, -font.width(text).toFloat() / 2, 0f, 0xFFFFFF, false, matrix4f, vertexConsumer,
            Font.DisplayMode.SEE_THROUGH,
            0, LightTexture.FULL_BRIGHT
        )
        vertexConsumer.endBatch()
        matrices.popPose()
    }

    fun renderWaypoint(
        text: Component,
        context: WorldRenderContext,
        pos: Vec3,
    ) {
        val layer = DulkirRenderTypes.DULKIR_QUADS_ESP
        val player = mc.player ?: return;
        val d: Double = pos.distanceTo(player.position())
        val distText = Component.literal(d.toInt().toString() + "m")
            .withStyle(ChatFormatting.YELLOW)
        val matrices = context.matrices() ?: return
        val bufferSource = context.worldRenderer().renderBuffers.bufferSource()
        matrices.pushPose()
        val camera = context.gameRenderer().mainCamera
        val magnitude = sqrt((pos.x - camera.position().x).pow(2) +
            (pos.y - camera.position().y).pow(2) +
                (pos.z - camera.position().z).pow(2))
        if (magnitude < 20) {
            matrices.translate(
                pos.x - camera.position().x,
                pos.y - camera.position().y,
                pos.z - camera.position().z
            )
        } else {
            matrices.translate(
                (pos.x - camera.position().x) / magnitude * 20,
                (pos.y - camera.position().y) / magnitude * 20,
                (pos.z - camera.position().z) / magnitude * 20
            )
        }
        matrices.mulPose(camera.rotation())
        val scale = max(d.toFloat() / 7f, 1f)
        if (magnitude < 20) {
            matrices.scale(.025f * scale, -.025f * scale, 1F)
        } else {
            matrices.scale(.025f * 20 / 7f, -.025f * 20 / 7f, .1F)
        }
        val matrix4f = matrices.last().pose()
        val font = mc.font
        val j: Int = (.25 * 255.0f).toInt() shl 24
        val buf = RenderUtil.getBufferFor(layer)
        buf.addVertex(matrix4f, -1.0f - font.width(text) / 2, -1.0f, 0.0f)
            .setColor(j)
            .setLight(LightTexture.FULL_BRIGHT)
        buf.addVertex(matrix4f, -1.0f - font.width(text) / 2, font.lineHeight.toFloat(), 0.0f)
            .setColor(j)
            .setLight(LightTexture.FULL_BRIGHT)
        buf.addVertex(matrix4f, font.width(text).toFloat() / 2, font.lineHeight.toFloat(), 0.0f)
            .setColor(j)
            .setLight(LightTexture.FULL_BRIGHT)
        buf.addVertex(matrix4f, font.width(text).toFloat() / 2, -1.0f, 0.0f)
            .setColor(j)
            .setLight(LightTexture.FULL_BRIGHT)

        matrices.translate(0F, 0F, 0.01F)
        layer.draw(buf.buildOrThrow())

        font.drawInBatch(
            text, -font.width(text).toFloat() / 2, 0f, 0xFFFFFF, false, matrix4f, bufferSource,
            Font.DisplayMode.SEE_THROUGH,
            0, LightTexture.FULL_BRIGHT
        )

        font.drawInBatch(
            distText, -font.width(distText).toFloat() / 2, 10f, 0xFFFFFF, false, matrix4f, bufferSource,
            Font.DisplayMode.SEE_THROUGH,
            0, LightTexture.FULL_BRIGHT
        )
        bufferSource.endBatch()

        matrices.popPose()
    }

    /**
     * Draws a filled box at a given position
     */
    fun drawBox(
        context: WorldRenderContext,
        x: Double,
        y: Double,
        z: Double,
        width: Double,
        height: Double,
        depth: Double,
        color: Color,
        depthTest: Boolean
    ) {
        val layer = if (depthTest) {
            DulkirRenderTypes.DULKIR_TRIANGLE_STRIP
        } else {
            DulkirRenderTypes.DULKIR_TRIANGLE_STRIP_ESP
        }

        val matrices = context.matrices() ?: return
        val buf = RenderUtil.getBufferFor(layer)
        matrices.pushPose()
        val camera = context.gameRenderer().mainCamera;
        matrices.translate(x - camera.position().x, y - camera.position().y, z - camera.position().z)
        // Note: color.rgb is a terrible name, this value is actually the argb int that we're looking for.
        addBoxVertices(matrices.last(), buf, 0.0, 0.0, 0.0, width, height, depth, color.rgb)
        layer.draw(buf.buildOrThrow())
        matrices.popPose()
    }

    private fun addBoxVertices(
        matrix: PoseStack.Pose, buf: BufferBuilder,
        x1: Double, y1: Double, z1: Double,
        x2: Double, y2: Double, z2: Double,
        color: Int
    ) {
        val mat = matrix.pose()
        fun v(x: Double, y: Double, z: Double) {
            buf.addVertex(mat, x.toFloat(), y.toFloat(), z.toFloat()).setColor(color)
        }

        v(x2, y2, z2)
        v(x2, y2, z2)
        v(x2, y2, z2)
        v(x2, y2, z1)
        v(x2, y1, z2)
        v(x2, y1, z1)
        v(x2, y1, z1)
        v(x2, y2, z1)
        v(x1, y1, z1)
        v(x1, y2, z1)
        v(x1, y2, z1)
        v(x1, y2, z2)
        v(x1, y1, z1)
        v(x1, y1, z2)
        v(x1, y1, z2)
        v(x1, y2, z2)
        v(x2, y1, z2)
        v(x2, y2, z2)
        v(x2, y2, z2)
        v(x1, y2, z2)
        v(x2, y2, z1)
        v(x1, y2, z1)
        v(x1, y2, z1)
        v(x2, y1, z2)
        v(x2, y1, z2)
        v(x2, y1, z1)
        v(x1, y1, z2)
        v(x1, y1, z1)
        v(x1, y1, z1)
        v(x1, y1, z1)
    }

    private fun line(
        matrix: PoseStack.Pose, buffer: BufferBuilder,
        x1: Number, y1: Number, z1: Number,
        x2: Number, y2: Number, z2: Number,
        color: Color, lineWidth: Float
    ) {
        val fov = mc.options.fov().get()
        val fovMultiplier = (fov / 70.0).toFloat() // Normalize to default FOV
        val finalWidth = lineWidth * fovMultiplier * 0.0025f
        thickLine(
            matrix, buffer,
            x1, y1, z1,
            x2, y2, z2,
            color.rgb, finalWidth
        )
    }

    // Renders a line as a camera-facing quad so thickness is actually respected.
    private fun thickLine(
        matrix: PoseStack.Pose, buffer: BufferBuilder,
        x1: Number, y1: Number, z1: Number,
        x2: Number, y2: Number, z2: Number,
        color: Int, halfWidth: Float
    ) {
        val fx = x1.toFloat(); val fy = y1.toFloat(); val fz = z1.toFloat()
        val tx = x2.toFloat(); val ty = y2.toFloat(); val tz = z2.toFloat()

        val dir = Vector3f(tx - fx, ty - fy, tz - fz)
        if (dir.lengthSquared() < 1e-6f) return
        dir.normalize()

        val camera = mc.gameRenderer.mainCamera
        val cameraForward = Vector3f(camera.forwardVector())
        val perp = dir.cross(cameraForward, Vector3f())

        if (perp.lengthSquared() < 1e-6f) {
            // Camera is looking along the line, use camera's up vector instead
            val cameraUp = Vector3f(camera.upVector())
            dir.cross(cameraUp, perp)
        }

        perp.normalize().mul(halfWidth)

        val mat = matrix.pose()
        buffer.addVertex(mat, fx - perp.x, fy - perp.y, fz - perp.z).setColor(color)
        buffer.addVertex(mat, fx + perp.x, fy + perp.y, fz + perp.z).setColor(color)
        buffer.addVertex(mat, tx + perp.x, ty + perp.y, tz + perp.z).setColor(color)
        buffer.addVertex(mat, tx - perp.x, ty - perp.y, tz - perp.z).setColor(color)
    }

}