package com.dulkirfabric.util.render

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import java.awt.Color
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt


object WorldRenderUtils {
    private fun line(
        matrix: MatrixStack.Entry, buffer: BufferBuilder,
        x1: Number, y1: Number, z1: Number,
        x2: Number, y2: Number, z2: Number,
        lineWidth: Float
    ) {
        val camera = MinecraftClient.getInstance().cameraEntity ?: return
        RenderSystem.lineWidth(lineWidth / camera.pos.squaredDistanceTo(
            Vec3d(x1.toDouble(), y1.toDouble(), z1.toDouble())
        ).pow(0.25).toFloat())
        line(
            matrix,
            buffer,
            Vector3f(x1.toFloat(), y1.toFloat(), z1.toFloat()),
            Vector3f(x2.toFloat(), y2.toFloat(), z2.toFloat())
        )
    }

    private fun line(matrix: MatrixStack.Entry, buffer: BufferBuilder, from: Vector3f, to: Vector3f) {
        val normal = to.sub(from, Vector3f()).mul(-1F)
        buffer.vertex(matrix.positionMatrix, from.x, from.y, from.z)
            .normal(matrix, normal.x, normal.y, normal.z)
                .color(0xFFFFFFFF.toInt())
        buffer.vertex(matrix.positionMatrix, to.x, to.y, to.z)
            .normal(matrix, normal.x, normal.y, normal.z)
                .color(0xFFFFFFFF.toInt())
    }

    fun drawWireFrame(
        context: WorldRenderContext,
        box: Box,
        color: Color,
        thickness: Float,
        depthTest: Boolean = true
    ) {
        val matrices = context.matrixStack() ?: return
        matrices.push()
        val layer = if (depthTest) {
            DulkirRenderLayer.DULKIR_LINES
        } else {
            DulkirRenderLayer.DULKIR_LINES_ESP
        }
        RenderSystem.setShaderColor(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)

        matrices.translate(-context.camera().pos.x, -context.camera().pos.y, -context.camera().pos.z)
        val buf = RenderUtil.getBufferFor(layer);
        val me = matrices.peek()

        buf.color(255, 255, 255, 255)

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

        layer.draw(buf.end())

        matrices.pop()
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F)
    }

    fun drawLine(context: WorldRenderContext, startPos: Vec3d, endPos: Vec3d, color: Color, thickness: Float, depthTest: Boolean = true) {
        val matrices = context.matrixStack() ?: return
        matrices.push()
        // RenderSystem.defaultBlendFunc()
        RenderSystem.setShaderColor(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
        val layer = if (depthTest) {
            DulkirRenderLayer.DULKIR_LINES
        } else {
            DulkirRenderLayer.DULKIR_LINES_ESP
        }
        matrices.translate(-context.camera().pos.x, -context.camera().pos.y, -context.camera().pos.z)
        val buf = RenderUtil.getBufferFor(layer)
        val me = matrices.peek()

        buf.color(255, 255, 255, 255)

        line(me, buf, startPos.x.toFloat(), startPos.y.toFloat(), startPos.z.toFloat(), endPos.x.toFloat(), endPos.y.toFloat(), endPos.z.toFloat(), thickness)

        layer.draw(buf.end())

        matrices.pop()
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F)
    }

    fun drawLineArray(context: WorldRenderContext, posArr: List<Vec3d>, color: Color, thickness: Float, depthTest: Boolean = true) {
        val matrices = context.matrixStack() ?: return
        matrices.push()
        RenderSystem.setShaderColor(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
        val layer = if (depthTest) {
            DulkirRenderLayer.DULKIR_LINES
        } else {
            DulkirRenderLayer.DULKIR_LINES_ESP
        }
        matrices.translate(-context.camera().pos.x, -context.camera().pos.y, -context.camera().pos.z)
        val buf = RenderUtil.getBufferFor(layer)
        val me = matrices.peek()

        buf.color(255, 255, 255, 255)

        for (i in 0 until posArr.size - 1) {
            val startPos = posArr[i]
            val endPos = posArr[i + 1]
            line(me, buf, startPos.x.toFloat(), startPos.y.toFloat(), startPos.z.toFloat(), endPos.x.toFloat(), endPos.y.toFloat(), endPos.z.toFloat(), thickness)
        }

        layer.draw(buf.end())

        matrices.pop()
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F)
    }

    /**
     * If you intend to show with distance, use a waypoint I think. If you're looking at this
     * statement and screaming at me for forgetting some use case, either let me know or compile
     * a method that accomplishes your goals based off of this example code. Neither of these
     * things should be incredibly difficult.
     */
    fun drawText(
        text: Text,
        context: WorldRenderContext,
        pos: Vec3d,
        depthTest: Boolean = true, // TODO
        scale: Float = 1f
    ) {
        val layer = DulkirRenderLayer.DULKIR_QUADS_ESP

        // Minecraft vertex consumer because we still hook into their renderer and do immediate text rendering
        val vertexConsumer = context.worldRenderer().bufferBuilders.entityVertexConsumers
        val matrices = context.matrixStack() ?: return
        matrices.push()
        matrices.translate(
            pos.x - context.camera().pos.x,
            pos.y - context.camera().pos.y,
            pos.z - context.camera().pos.z
        )
        matrices.multiply(context.camera().rotation)
        matrices.scale(.025f * scale, -.025f * scale, 1F)
        val matrix4f = matrices.peek().positionMatrix
        val textRenderer = MinecraftClient.getInstance().textRenderer
        val j: Int = (.25 * 255.0f).toInt() shl 24
        val buf = RenderUtil.getBufferFor(layer)
        buf.vertex(matrix4f, -1.0f - textRenderer.getWidth(text) / 2, -1.0f, 0.0f)
            .color(j)
            .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
        buf.vertex(matrix4f, -1.0f - textRenderer.getWidth(text) / 2, textRenderer.fontHeight.toFloat(), 0.0f)
            .color(j)
            .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
        buf.vertex(matrix4f, textRenderer.getWidth(text).toFloat() / 2, textRenderer.fontHeight.toFloat(), 0.0f)
            .color(j)
            .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
        buf.vertex(matrix4f, textRenderer.getWidth(text).toFloat() / 2, -1.0f, 0.0f)
            .color(j)
            .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)

        matrices.translate(0F, 0F, 0.01F)
        layer.draw(buf.end())
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F)
        textRenderer.draw(
            text, -textRenderer.getWidth(text).toFloat() / 2, 0f, 0xFFFFFF, false, matrix4f, vertexConsumer,
            TextRenderer.TextLayerType.SEE_THROUGH,
            0, LightmapTextureManager.MAX_LIGHT_COORDINATE
        )
        vertexConsumer.drawCurrentLayer()
        matrices.pop()
    }

    fun renderWaypoint(
        text: Text,
        context: WorldRenderContext,
        pos: Vec3d,
    )
    {
        val layer = DulkirRenderLayer.DULKIR_QUADS_ESP
        val d: Double = pos.distanceTo(MinecraftClient.getInstance().player?.pos)
        val distText = Text.literal(d.toInt().toString() + "m").setStyle(Style.EMPTY.withColor(Formatting.YELLOW))
        val matrices = context.matrixStack() ?: return
        val vertexConsumer = context.worldRenderer().bufferBuilders.entityVertexConsumers
        matrices.push()
        val magnitude = sqrt((pos.x - context.camera().pos.x).pow(2) +
            (pos.y - context.camera().pos.y).pow(2) +
                (pos.z - context.camera().pos.z).pow(2))
        if (magnitude < 20) {
            matrices.translate(
                pos.x - context.camera().pos.x,
                pos.y - context.camera().pos.y,
                pos.z - context.camera().pos.z
            )
        } else {
            matrices.translate(
                (pos.x - context.camera().pos.x) / magnitude * 20,
                (pos.y - context.camera().pos.y) / magnitude * 20,
                (pos.z - context.camera().pos.z) / magnitude * 20
            )
        }
        matrices.multiply(context.camera().rotation)
        val scale = max(d.toFloat() / 7f, 1f)
        if (magnitude < 20) {
            matrices.scale(.025f * scale, -.025f * scale, 1F)
        } else {
            matrices.scale(.025f * 20 / 7f, -.025f * 20 / 7f, .1F)
        }
        val matrix4f = matrices.peek().positionMatrix
        val textRenderer = MinecraftClient.getInstance().textRenderer
        val j: Int = (.25 * 255.0f).toInt() shl 24
        val buf = RenderUtil.getBufferFor(layer)
        buf.vertex(matrix4f, -1.0f - textRenderer.getWidth(text) / 2, -1.0f, 0.0f)
            .color(j)
            .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
        buf.vertex(matrix4f, -1.0f - textRenderer.getWidth(text) / 2, textRenderer.fontHeight.toFloat(), 0.0f)
            .color(j)
            .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
        buf.vertex(matrix4f, textRenderer.getWidth(text).toFloat() / 2, textRenderer.fontHeight.toFloat(), 0.0f)
            .color(j)
            .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
        buf.vertex(matrix4f, textRenderer.getWidth(text).toFloat() / 2, -1.0f, 0.0f)
            .color(j)
            .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)

        matrices.translate(0F, 0F, 0.01F)
        layer.draw(buf.end())

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F)
        textRenderer.draw(
            text, -textRenderer.getWidth(text).toFloat() / 2, 0f, 0xFFFFFF, false, matrix4f, vertexConsumer,
            TextRenderer.TextLayerType.SEE_THROUGH,
            0, LightmapTextureManager.MAX_LIGHT_COORDINATE
        )

        textRenderer.draw(
            distText, -textRenderer.getWidth(distText).toFloat() / 2, 10f, 0xFFFFFF, false, matrix4f, vertexConsumer,
            TextRenderer.TextLayerType.SEE_THROUGH,
            0, LightmapTextureManager.MAX_LIGHT_COORDINATE
        )
        vertexConsumer.draw()

        matrices.pop()
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
            DulkirRenderLayer.DULKIR_TRIANGLE_STRIP
        } else {
            DulkirRenderLayer.DULKIR_TRIANGLE_STRIP_ESP
        }

        val matrices = context.matrixStack() ?: return
        val buf = RenderUtil.getBufferFor(layer)
        matrices.push()
        matrices.translate(x - context.camera().pos.x, y - context.camera().pos.y, z - context.camera().pos.z)
        VertexRendering.drawFilledBox(matrices, buf, 0.0, 0.0, 0.0, width, height, depth,
            color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
        layer.draw(buf.end())
        matrices.pop()
    }
}