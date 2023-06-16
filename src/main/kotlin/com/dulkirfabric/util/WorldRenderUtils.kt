package com.dulkirfabric.util

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import java.awt.Color
import kotlin.math.pow


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
    fun drawBox(
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
}