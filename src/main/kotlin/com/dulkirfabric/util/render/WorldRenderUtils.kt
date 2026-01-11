package com.dulkirfabric.util.render

import com.dulkirfabric.DulkirModFabric.mc
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.ShapeRenderer
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import java.awt.Color
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

object WorldRenderUtils {
    private fun line(
        matrix: PoseStack.Pose, buffer: BufferBuilder,
        x1: Number, y1: Number, z1: Number,
        x2: Number, y2: Number, z2: Number,
        lineWidth: Float
    ) {
        val camera = mc.cameraEntity ?: return
        RenderSystem.lineWidth(lineWidth / camera.position().distanceToSqr(
            Vec3(x1.toDouble(), y1.toDouble(), z1.toDouble())
        ).pow(0.25).toFloat())
        line(
            matrix,
            buffer,
            Vector3f(x1.toFloat(), y1.toFloat(), z1.toFloat()),
            Vector3f(x2.toFloat(), y2.toFloat(), z2.toFloat())
        )
    }

    private fun line(matrix: PoseStack.Pose, buffer: BufferBuilder, from: Vector3f, to: Vector3f) {
        val normal = to.sub(from, Vector3f()).mul(-1F)
        buffer.addVertex(matrix.pose(), from.x, from.y, from.z)
            .setNormal(matrix, normal.x, normal.y, normal.z)
                .setColor(0xFFFFFFFF.toInt())
        buffer.addVertex(matrix.pose(), to.x, to.y, to.z)
            .setNormal(matrix, normal.x, normal.y, normal.z)
                .setColor(0xFFFFFFFF.toInt())
    }

    fun drawWireFrame(
        context: WorldRenderContext,
        box: AABB,
        color: Color,
        thickness: Float,
        depthTest: Boolean = true
    ) {
        val matrices = context.matrices() ?: return
        matrices.pushPose()
        val layer = if (depthTest) {
            DulkirRenderTypes.DULKIR_LINES
        } else {
            DulkirRenderTypes.DULKIR_LINES_ESP
        }
        val camera = context.gameRenderer().mainCamera;
        matrices.translate(-camera.position.x, -camera.position.y, -camera.position.z)
        val buf = RenderUtil.getBufferFor(layer);
        val me = matrices.last()

        buf.setColor(color.red, color.green, color.blue, color.alpha)

        // bottom
        line(me, buf, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ, thickness)
        line(me, buf, box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ, thickness)
        line(me, buf, box.maxX, box.minY, box.maxZ, box.minX, box.minY, box.maxZ, thickness)
        line(me, buf, box.minX, box.minY, box.maxZ, box.minX, box.minY, box.minZ, thickness)

        line(me, buf, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ, thickness)

        // top
        line(me, buf, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ, thickness)
        line(me, buf, box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ, thickness)
        line(me, buf, box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ, thickness)
        line(me, buf, box.minX, box.maxY, box.maxZ, box.minX, box.maxY, box.minZ, thickness)

        // some redraws (blame strips) and getting the rest of the vertical columns
        line(me, buf, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ, thickness)
        line(me, buf, box.maxX, box.maxY, box.minZ, box.maxX, box.minY, box.minZ, thickness)
        line(me, buf, box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ, thickness)
        line(me, buf, box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ, thickness)
        line(me, buf, box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ, thickness)
        line(me, buf, box.minX, box.maxY, box.maxZ, box.minX, box.minY, box.maxZ, thickness)

        layer.draw(buf.buildOrThrow())

        matrices.popPose()
    }

    fun drawLine(context: WorldRenderContext, startPos: Vec3, endPos: Vec3, color: Color, thickness: Float, depthTest: Boolean = true) {
        val matrices = context.matrices() ?: return
        matrices.pushPose()
        val layer = if (depthTest) {
            DulkirRenderTypes.DULKIR_LINES
        } else {
            DulkirRenderTypes.DULKIR_LINES_ESP
        }
        val camera = context.gameRenderer().mainCamera;
        matrices.translate(-camera.position.x, -camera.position.y, -camera.position.z)
        val buf = RenderUtil.getBufferFor(layer)
        val me = matrices.last()

        buf.setColor(color.red, color.green, color.blue, color.alpha)

        line(me, buf, startPos.x.toFloat(), startPos.y.toFloat(), startPos.z.toFloat(), endPos.x.toFloat(), endPos.y.toFloat(), endPos.z.toFloat(), thickness)

        layer.draw(buf.buildOrThrow())

        matrices.popPose()
    }

    fun drawLineArray(context: WorldRenderContext, posArr: List<Vec3>, color: Color, thickness: Float,
                      depthTest: Boolean = true) {
        val matrices = context.matrices() ?: return
        matrices.pushPose()
        val layer = if (depthTest) {
            DulkirRenderTypes.DULKIR_LINES
        } else {
            DulkirRenderTypes.DULKIR_LINES_ESP
        }
        val camera = context.gameRenderer().mainCamera;
        matrices.translate(-camera.position.x, -camera.position.y, -camera.position.z)
        val buf = RenderUtil.getBufferFor(layer)
        val me = matrices.last()

        buf.setColor(color.red, color.green, color.blue, color.alpha)

        for (i in 0 until posArr.size - 1) {
            val startPos = posArr[i]
            val endPos = posArr[i + 1]
            line(me, buf, startPos.x.toFloat(), startPos.y.toFloat(), startPos.z.toFloat(), endPos.x.toFloat(), endPos.y.toFloat(), endPos.z.toFloat(), thickness)
        }

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
            pos.x - camera.position.x,
            pos.y - camera.position.y,
            pos.z - camera.position.z
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
    )
    {
        val layer = DulkirRenderTypes.DULKIR_QUADS_ESP
        val player = mc.player ?: return;
        val d: Double = pos.distanceTo(player.position())
        val distText = Component.literal(d.toInt().toString() + "m")
            .withStyle(ChatFormatting.YELLOW)
        val matrices = context.matrices() ?: return
        val bufferSource = context.worldRenderer().renderBuffers.bufferSource()
        matrices.pushPose()
        val camera = context.gameRenderer().mainCamera
        val magnitude = sqrt((pos.x - camera.position.x).pow(2) +
            (pos.y - camera.position.y).pow(2) +
                (pos.z - camera.position.z).pow(2))
        if (magnitude < 20) {
            matrices.translate(
                pos.x - camera.position.x,
                pos.y - camera.position.y,
                pos.z - camera.position.z
            )
        } else {
            matrices.translate(
                (pos.x - camera.position.x) / magnitude * 20,
                (pos.y - camera.position.y) / magnitude * 20,
                (pos.z - camera.position.z) / magnitude * 20
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
        matrices.translate(x - camera.position.x, y - camera.position.y, z - camera.position.z)
        ShapeRenderer.addChainedFilledBoxVertices(matrices, buf, 0.0, 0.0, 0.0, width, height, depth,
            color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
        layer.draw(buf.buildOrThrow())
        matrices.popPose()
    }
}