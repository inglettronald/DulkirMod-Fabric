package com.dulkirfabric.util

import com.mojang.blaze3d.platform.GlConst
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction.Axis
import org.joml.Vector3f
import java.awt.Color


object WorldRenderUtils {
    private fun line(
        matrix: MatrixStack.Entry, buffer: BufferBuilder,
        x1: Number, y1: Number, z1: Number,
        x2: Number, y2: Number, z2: Number,
    ) {
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

    fun drawBox(
        context: WorldRenderContext,
        box: Box,
        color: Color,
        thickness: Float,
        depthTest: Boolean = true
    ) {
        val matrices = context.matrixStack()
        matrices.push()
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram)
        RenderSystem.disableBlend()
        RenderSystem.disableCull()
        RenderSystem.defaultBlendFunc()
        RenderSystem.lineWidth(thickness)
        RenderSystem.setShaderColor(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
        if (!depthTest) {
            RenderSystem.disableDepthTest()
        } else {
            RenderSystem.enableDepthTest()
            RenderSystem.depthFunc(GlConst.GL_LEQUAL)
            RenderSystem.depthMask(false)
        }
        matrices.translate(-context.camera().pos.x, -context.camera().pos.y, -context.camera().pos.z)
        val tess = RenderSystem.renderThreadTesselator()
        val buf = tess.buffer
        val me = matrices.peek()

        buf.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES)
        buf.fixedColor(255, 255, 255, 255)

        // X Axis aligned lines
        line(me, buf, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ)
        line(me, buf, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ)
        line(me, buf, box.minX, box.minY, box.maxZ, box.maxX, box.minY, box.maxZ)
        line(me, buf, box.minX, box.maxY, box.maxZ, box.maxX, box.maxY, box.maxZ)

        // Y Axis aligned lines
        line(me, buf, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ)
        line(me, buf, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ)
        line(me, buf, box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ)
        line(me, buf, box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ)

        // Z Axis aligned lines
        line(me, buf, box.minX, box.minY, box.minZ, box.minX, box.minY, box.maxZ)
        line(me, buf, box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ)
        line(me, buf, box.minX, box.maxY, box.minZ, box.minX, box.maxY, box.maxZ)
        line(me, buf, box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ)

        buf.unfixColor()
        tess.draw()


        RenderSystem.enableDepthTest()
        RenderSystem.disableBlend()
        RenderSystem.setShaderColor(
            1f, 1f, 1f, 1f
        )
        matrices.pop()
    }
}