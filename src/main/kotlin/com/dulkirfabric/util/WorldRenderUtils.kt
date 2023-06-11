package com.dulkirfabric.util

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import java.awt.Color


object WorldRenderUtils {
    fun drawBox(
        context: WorldRenderContext,
        x1: Float,
        y1: Float,
        z1: Float,
        x2: Float,
        y2: Float,
        z2: Float,
        color: Color,
        thickness: Float,
        depthTest: Boolean = true
    ) {
        val matrices = context.matrixStack()
        matrices.push()
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram)
        RenderSystem.enableBlend()
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA)
        RenderSystem.lineWidth(thickness)
        RenderSystem.setShaderColor(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
        if (!depthTest) RenderSystem.disableDepthTest()
        matrices.translate(-context.camera().pos.x, -context.camera().pos.y, -context.camera().pos.z)
        val tess = Tessellator.getInstance()
        val buf = tess.buffer
        val matrix4f = matrices.peek().positionMatrix

        buf.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR)
        buf.fixedColor(255, 255, 255, 255)

        buf.vertex(matrix4f, x1, y1, z1).next()
        buf.vertex(matrix4f, x2, y1, z1).next()

        //buf.vertex(matrix4f, x2, y2, z1).next()

        buf.unfixColor()
        tess.draw()


        RenderSystem.enableDepthTest()
        RenderSystem.disableBlend()
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        matrices.pop()
    }
}