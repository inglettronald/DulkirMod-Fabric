package com.dulkirfabric.util.render

import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderLayer.MultiPhase
import net.minecraft.client.render.RenderPhase

object DulkirRenderLayers {

    val DULKIR_LINES: MultiPhase = RenderLayer.of(
        "dulkir-lines",
        RenderLayer.DEFAULT_BUFFER_SIZE,
        DulkirRenderPipelines.DULKIR_LINES,
        RenderLayer.MultiPhaseParameters.Builder()
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false)
    )

    val DULKIR_LINES_ESP: MultiPhase = RenderLayer.of(
        "dulkir-lines-esp",
        RenderLayer.DEFAULT_BUFFER_SIZE,
        DulkirRenderPipelines.DULKIR_LINES_ESP,
        RenderLayer.MultiPhaseParameters.Builder().build(false)
    )

    val DULKIR_TEXT: MultiPhase = RenderLayer.of(
        "dulkir-text",
        RenderLayer.DEFAULT_BUFFER_SIZE,
        DulkirRenderPipelines.DULKIR_TEXT,
        RenderLayer.MultiPhaseParameters.Builder()
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false)
    )

    val DULKIR_TEXT_ESP: MultiPhase = RenderLayer.of(
        "dulkir_text_esp",
        RenderLayer.DEFAULT_BUFFER_SIZE,
        DulkirRenderPipelines.DULKIR_TEXT_ESP,
        RenderLayer.MultiPhaseParameters.Builder().build(false)
    )

    val DULKIR_TRIANGLE_STRIP: MultiPhase = RenderLayer.of(
        "dulkir_triangle_strip",
        RenderLayer.DEFAULT_BUFFER_SIZE,
        false,
        true,
        DulkirRenderPipelines.DULKIR_TRIANGLE_STRIP,
        RenderLayer.MultiPhaseParameters.Builder()
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false)
    )

    val DULKIR_TRIANGLE_STRIP_ESP: MultiPhase = RenderLayer.of(
        "dulkir_triangle_strip",
        RenderLayer.DEFAULT_BUFFER_SIZE,
        false,
        true,
        DulkirRenderPipelines.DULKIR_TRIANGLE_STRIP_ESP,
        RenderLayer.MultiPhaseParameters.Builder().build(false)
    )

    val DULKIR_QUADS_ESP: MultiPhase = RenderLayer.of(
        "dulkir_quads",
        RenderLayer.DEFAULT_BUFFER_SIZE,
        false,
        true,
        DulkirRenderPipelines.DULKIR_QUADS_ESP,
        RenderLayer.MultiPhaseParameters.Builder().build(false)
    )

    val LAYERS: Set<RenderLayer> = setOf(
        DULKIR_LINES,
        DULKIR_LINES_ESP,
        DULKIR_TEXT,
        DULKIR_TEXT_ESP,
        DULKIR_TRIANGLE_STRIP,
        DULKIR_TRIANGLE_STRIP_ESP
    )

}