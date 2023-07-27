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
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.max
import kotlin.math.min
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
            .normal(matrix.normalMatrix, normal.x, normal.y, normal.z).next()
        buffer.vertex(matrix.positionMatrix, to.x, to.y, to.z)
            .normal(matrix.normalMatrix, normal.x, normal.y, normal.z)
            .next()
    }

    /**
     * Draws a box in world space, given the coordinates of the box, thickness of the lines, and color.
     * TODO: write a more custom rendering function so we don't have to do this ugly translation of
     * Minecraft's screen space rendering logic to a world space rendering function.
     */
    fun drawWireFrame(
        context: WorldRenderContext,
        box: Box,
        color: Color,
        thickness: Float,
        depthTest: Boolean = true
    ) {
        val matrices = context.matrixStack()
        matrices.push()
        val prevShader = RenderSystem.getShader()
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram)
        RenderSystem.disableBlend()
        RenderSystem.disableCull()
        // RenderSystem.defaultBlendFunc()
        RenderSystem.setShaderColor(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
        if (!depthTest) {
            RenderSystem.disableDepthTest()
            RenderSystem.depthMask(false)
        } else {
            RenderSystem.enableDepthTest()
        }
        matrices.translate(-context.camera().pos.x, -context.camera().pos.y, -context.camera().pos.z)
        val tess = RenderSystem.renderThreadTesselator()
        val buf = tess.buffer
        val me = matrices.peek()

        buf.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES)
        buf.fixedColor(255, 255, 255, 255)

        // X Axis aligned lines
        line(me, buf, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ, thickness)
        line(me, buf, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ, thickness)
        line(me, buf, box.minX, box.minY, box.maxZ, box.maxX, box.minY, box.maxZ, thickness)
        line(me, buf, box.minX, box.maxY, box.maxZ, box.maxX, box.maxY, box.maxZ, thickness)

        // Y Axis aligned lines
        line(me, buf, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ, thickness)
        line(me, buf, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ, thickness)
        line(me, buf, box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ, thickness)
        line(me, buf, box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ, thickness)

        // Z Axis aligned lines
        line(me, buf, box.minX, box.minY, box.minZ, box.minX, box.minY, box.maxZ, thickness)
        line(me, buf, box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ, thickness)
        line(me, buf, box.minX, box.maxY, box.minZ, box.minX, box.maxY, box.maxZ, thickness)
        line(me, buf, box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ, thickness)

        buf.unfixColor()
        tess.draw()

        RenderSystem.depthMask(true)
        RenderSystem.enableDepthTest()
        RenderSystem.enableBlend()
        RenderSystem.setShaderColor(
            1f, 1f, 1f, 1f
        )
        RenderSystem.setShader { prevShader }
        RenderSystem.enableCull()
        matrices.pop()
    }

    /**
     * This draw line function is intended to be used for drawing very few lines, as it's not the most efficient.
     * For drawing many lines in a series, save them to an array and use the drawLineArray function.
     */
    fun drawLine(context: WorldRenderContext, startPos: Vec3d, endPos: Vec3d, color: Color, thickness: Float, depthTest: Boolean = true) {
        val matrices = context.matrixStack()
        matrices.push()
        val prevShader = RenderSystem.getShader()
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram)
        RenderSystem.disableBlend()
        RenderSystem.disableCull()
        // RenderSystem.defaultBlendFunc()
        RenderSystem.setShaderColor(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
        if (!depthTest) {
            RenderSystem.disableDepthTest()
            RenderSystem.depthMask(false)
        } else {
            RenderSystem.enableDepthTest()
        }
        matrices.translate(-context.camera().pos.x, -context.camera().pos.y, -context.camera().pos.z)
        val tess = RenderSystem.renderThreadTesselator()
        val buf = tess.buffer
        val me = matrices.peek()

        buf.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES)
        buf.fixedColor(255, 255, 255, 255)

        line(me, buf, startPos.x.toFloat(), startPos.y.toFloat(), startPos.z.toFloat(), endPos.x.toFloat(), endPos.y.toFloat(), endPos.z.toFloat(), thickness)

        buf.unfixColor()
        tess.draw()

        RenderSystem.depthMask(true)
        RenderSystem.enableDepthTest()
        RenderSystem.enableBlend()
        RenderSystem.setShaderColor(
            1f, 1f, 1f, 1f
        )
        RenderSystem.setShader { prevShader }
        RenderSystem.enableCull()
        matrices.pop()
    }

    /**
     * This function is intended to be used for drawing many lines in a series, as it's more efficient than the
     * drawLine function being called many times in series.
     */
    fun drawLineArray(context: WorldRenderContext, posArr: List<Vec3d>, color: Color, thickness: Float, depthTest: Boolean = true) {
        val matrices = context.matrixStack()
        matrices.push()
        val prevShader = RenderSystem.getShader()
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram)
        RenderSystem.disableBlend()
        RenderSystem.disableCull()
        // RenderSystem.defaultBlendFunc()
        RenderSystem.setShaderColor(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
        if (!depthTest) {
            RenderSystem.disableDepthTest()
            RenderSystem.depthMask(false)
        } else {
            RenderSystem.enableDepthTest()
        }
        matrices.translate(-context.camera().pos.x, -context.camera().pos.y, -context.camera().pos.z)
        val tess = RenderSystem.renderThreadTesselator()
        val buf = tess.buffer
        val me = matrices.peek()

        buf.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES)
        buf.fixedColor(255, 255, 255, 255)

        for (i in 0 until posArr.size - 1) {
            val startPos = posArr[i]
            val endPos = posArr[i + 1]
            line(me, buf, startPos.x.toFloat(), startPos.y.toFloat(), startPos.z.toFloat(), endPos.x.toFloat(), endPos.y.toFloat(), endPos.z.toFloat(), thickness)
        }

        buf.unfixColor()
        tess.draw()

        RenderSystem.depthMask(true)
        RenderSystem.enableDepthTest()
        RenderSystem.enableBlend()
        RenderSystem.setShaderColor(
            1f, 1f, 1f, 1f
        )
        RenderSystem.setShader { prevShader }
        RenderSystem.enableCull()
        matrices.pop()
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
        depthTest: Boolean = true,
        scale: Float = 1f
    ) {
        if (!depthTest) {
            RenderSystem.disableDepthTest()
        }
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.disableCull()

        val vertexConsumer = context.worldRenderer().bufferBuilders.entityVertexConsumers
        val matrices = context.matrixStack()
        matrices.push()
        matrices.translate(
            pos.x - context.camera().pos.x,
            pos.y - context.camera().pos.y,
            pos.z - context.camera().pos.z
        )
        matrices.multiply(context.camera().rotation)
        matrices.scale(-.025f * scale, -.025f * scale, -1F)
        val matrix4f = matrices.peek().positionMatrix
        val textRenderer = MinecraftClient.getInstance().textRenderer
        val j: Int = (.25 * 255.0f).toInt() shl 24
        val buf = vertexConsumer.getBuffer(RenderLayer.getTextBackgroundSeeThrough())
        buf.vertex(matrix4f, -1.0f - textRenderer.getWidth(text) / 2, -1.0f, 0.0f)
            .color(j)
            .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
            .next()
        buf.vertex(matrix4f, -1.0f - textRenderer.getWidth(text) / 2, textRenderer.fontHeight.toFloat(), 0.0f)
            .color(j)
            .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
            .next()
        buf.vertex(matrix4f, textRenderer.getWidth(text).toFloat() / 2, textRenderer.fontHeight.toFloat(), 0.0f)
            .color(j)
            .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
            .next()
        buf.vertex(matrix4f, textRenderer.getWidth(text).toFloat() / 2, -1.0f, 0.0f)
            .color(j)
            .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
            .next()

        matrices.translate(0F, 0F, 0.01F)

        textRenderer.draw(
            text, -textRenderer.getWidth(text).toFloat() / 2, 0f, 0xFFFFFF, false, matrix4f, vertexConsumer,
            TextRenderer.TextLayerType.SEE_THROUGH,
            0, LightmapTextureManager.MAX_LIGHT_COORDINATE
        )

        vertexConsumer.drawCurrentLayer()
        matrices.pop()
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F)
        RenderSystem.enableDepthTest()
        RenderSystem.enableCull()
        RenderSystem.disableBlend()
    }

    fun renderWaypoint(
        text: Text,
        context: WorldRenderContext,
        pos: Vec3d,
    )
    {
        RenderSystem.disableDepthTest()
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.disableCull()
        val d: Double = pos.distanceTo(MinecraftClient.getInstance().player?.pos)
        val distText = Text.literal(d.toInt().toString() + "m").setStyle(Style.EMPTY.withColor(Formatting.YELLOW))
        val matrices = context.matrixStack()
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
            matrices.scale(-.025f * scale, -.025f * scale, -1F)
        } else {
            matrices.scale(-.025f * 20 / 7f, -.025f * 20 / 7f, -.1F)
        }
        val matrix4f = matrices.peek().positionMatrix
        val textRenderer = MinecraftClient.getInstance().textRenderer
        val j: Int = (.25 * 255.0f).toInt() shl 24
        val buf = vertexConsumer.getBuffer(RenderLayer.getTextBackgroundSeeThrough())
        buf.vertex(matrix4f, -1.0f - textRenderer.getWidth(text) / 2, -1.0f, 0.0f)
            .color(j)
            .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
            .next()
        buf.vertex(matrix4f, -1.0f - textRenderer.getWidth(text) / 2, textRenderer.fontHeight.toFloat(), 0.0f)
            .color(j)
            .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
            .next()
        buf.vertex(matrix4f, textRenderer.getWidth(text).toFloat() / 2, textRenderer.fontHeight.toFloat(), 0.0f)
            .color(j)
            .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
            .next()
        buf.vertex(matrix4f, textRenderer.getWidth(text).toFloat() / 2, -1.0f, 0.0f)
            .color(j)
            .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
            .next()

        matrices.translate(0F, 0F, 0.01F)

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
        vertexConsumer.drawCurrentLayer()
        matrices.pop()
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F)
        RenderSystem.enableDepthTest()
        RenderSystem.enableCull()
        RenderSystem.disableBlend()
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
        if (!depthTest) {
            RenderSystem.disableDepthTest()
            //RenderSystem.depthMask(false)
        } else {
            RenderSystem.enableDepthTest()
        }
        RenderSystem.setShader(GameRenderer::getPositionColorProgram)
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()

        val matrices = context.matrixStack()
        val tes = Tessellator.getInstance()
        tes.buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR)
        matrices.push()
        matrices.translate(x - context.camera().pos.x, y - context.camera().pos.y, z - context.camera().pos.z)
        WorldRenderer.renderFilledBox(matrices, tes.buffer, 0.0, 0.0, 0.0, width, height, depth,
            color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
        tes.draw()
        RenderSystem.enableDepthTest()
        matrices.pop()
    }
}