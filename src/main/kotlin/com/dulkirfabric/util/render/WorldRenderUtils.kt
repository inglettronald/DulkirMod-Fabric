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
import java.awt.Color
import kotlin.math.abs
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
        val layer = if (depthTest) DulkirRenderTypes.DULKIR_TRIANGLE_STRIP else DulkirRenderTypes.DULKIR_TRIANGLE_STRIP_ESP
        val camera = context.gameRenderer().mainCamera
        val camPos = camera.position()

        matrices.pushPose()
        matrices.translate(-camPos.x, -camPos.y, -camPos.z)

        val buf = RenderUtil.getBufferFor(layer)
        val me = matrices.last()

        fun axisAlignedLine(
            x1: Double, y1: Double, z1: Double,
            x2: Double, y2: Double, z2: Double
        ) {
            val midX = (x1 + x2) / 2.0
            val midY = (y1 + y2) / 2.0
            val midZ = (z1 + z2) / 2.0

            // Calculate distance-based thickness normalization
            val dist = sqrt((midX - camPos.x).pow(2) + (midY - camPos.y).pow(2) + (midZ - camPos.z).pow(2))
            val finalHalfWidth = thickness * 0.001 * (dist / 2.0)

            var lineAABB = AABB(x1, y1, z1, x2, y2, z2)
            lineAABB = when {
                (abs(x2 - y1) > 0) -> lineAABB.inflate(finalHalfWidth / 2.0, finalHalfWidth, finalHalfWidth)
                (abs(y2 - y1) > 0) -> lineAABB.inflate(finalHalfWidth, finalHalfWidth / 2.0, finalHalfWidth)
                else -> lineAABB.inflate(finalHalfWidth, finalHalfWidth, finalHalfWidth / 2.0)
            }

            addBoxVertices(
                me, buf,
                lineAABB.minX, lineAABB.minY, lineAABB.minZ,
                lineAABB.maxX, lineAABB.maxY, lineAABB.maxZ,
                color.rgb
            )
        }

        // bottom
        axisAlignedLine(box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ)
        axisAlignedLine(box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ)
        axisAlignedLine(box.maxX, box.minY, box.maxZ, box.minX, box.minY, box.maxZ)
        axisAlignedLine(box.minX, box.minY, box.maxZ, box.minX, box.minY, box.minZ)

        axisAlignedLine(box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ)
        axisAlignedLine(box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ)
        axisAlignedLine(box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ)
        axisAlignedLine(box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ)

        // top
        axisAlignedLine(box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ)
        axisAlignedLine(box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ)
        axisAlignedLine(box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ)
        axisAlignedLine(box.minX, box.maxY, box.maxZ, box.minX, box.maxY, box.minZ)

        layer.draw(buf.buildOrThrow())
        matrices.popPose()
    }

    fun renderWaypoint(
        text: Component,
        context: WorldRenderContext,
        pos: Vec3,
        dist: Boolean = true
    ) {
        val layer = DulkirRenderTypes.DULKIR_QUADS_ESP
        val player = mc.player ?: return;
        val d: Double = pos.distanceTo(player.position())
        val matrices = context.matrices() ?: return
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
        val font = mc.font
        val j: Int = (.25 * 255.0f).toInt() shl 24

        val buf = RenderUtil.getBufferFor(layer)
        val matrix4f = matrices.last().pose()
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
        layer.draw(buf.buildOrThrow())

        // Translate forward for text rendering
        matrices.translate(0F, 0F, 0.01F)
        val textMatrix = matrices.last().pose()
        val bufferSource = context.consumers() ?: return

        font.drawInBatch(
            text, -font.width(text).toFloat() / 2, 0f, 0xFFFFFFFF.toInt(), false, textMatrix, bufferSource,
            Font.DisplayMode.SEE_THROUGH,
            0, LightTexture.FULL_BRIGHT
        )

        if (dist) {
            val distText = Component.literal(d.toInt().toString() + "m")
                .withStyle(ChatFormatting.YELLOW)
            font.drawInBatch(
                distText, -font.width(distText).toFloat() / 2, 10f, 0xFFFFFFFF.toInt(), false, textMatrix, bufferSource,
                Font.DisplayMode.SEE_THROUGH,
                0, LightTexture.FULL_BRIGHT
            )
        }

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

}