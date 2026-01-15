package com.dulkirfabric.util.render

import net.minecraft.client.renderer.RenderType

object DulkirRenderTypes {

    val DULKIR_LINES: RenderType.CompositeRenderType = RenderType.create(
        "dulkir-lines",
        RenderType.TRANSIENT_BUFFER_SIZE,
        DulkirRenderPipelines.DULKIR_LINES,
        RenderType.CompositeState.builder()
            .setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
            .createCompositeState(false)
    )

    val DULKIR_LINES_ESP: RenderType.CompositeRenderType  = RenderType.create(
        "dulkir-lines-esp",
        RenderType.TRANSIENT_BUFFER_SIZE,
        DulkirRenderPipelines.DULKIR_LINES_ESP,
        RenderType.CompositeState.builder().createCompositeState(false)
    )

    val DULKIR_TEXT: RenderType.CompositeRenderType  = RenderType.create(
        "dulkir-text",
        RenderType.TRANSIENT_BUFFER_SIZE,
        DulkirRenderPipelines.DULKIR_TEXT,
        RenderType.CompositeState.builder()
            .setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
            .createCompositeState(false)
    )

    val DULKIR_TEXT_ESP: RenderType.CompositeRenderType  = RenderType.create(
        "dulkir_text_esp",
        RenderType.TRANSIENT_BUFFER_SIZE,
        DulkirRenderPipelines.DULKIR_TEXT_ESP,
        RenderType.CompositeState.builder().createCompositeState(false)
    )

    val DULKIR_TRIANGLE_STRIP: RenderType.CompositeRenderType  = RenderType.create(
        "dulkir_triangle_strip",
        RenderType.TRANSIENT_BUFFER_SIZE,
        false,
        true,
        DulkirRenderPipelines.DULKIR_TRIANGLE_STRIP,
        RenderType.CompositeState.builder()
            .setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
            .createCompositeState(false)
    )

    val DULKIR_TRIANGLE_STRIP_ESP: RenderType.CompositeRenderType  = RenderType.create(
        "dulkir_triangle_strip",
        RenderType.TRANSIENT_BUFFER_SIZE,
        false,
        true,
        DulkirRenderPipelines.DULKIR_TRIANGLE_STRIP_ESP,
        RenderType.CompositeState.builder().createCompositeState(false)
    )

    val DULKIR_QUADS_ESP: RenderType.CompositeRenderType  = RenderType.create(
        "dulkir_quads",
        RenderType.TRANSIENT_BUFFER_SIZE,
        false,
        true,
        DulkirRenderPipelines.DULKIR_QUADS_ESP,
        RenderType.CompositeState.builder().createCompositeState(false)
    )

    val TYPES: Set<RenderType> = setOf(
        DULKIR_LINES,
        DULKIR_LINES_ESP,
        DULKIR_TEXT,
        DULKIR_TEXT_ESP,
        DULKIR_TRIANGLE_STRIP,
        DULKIR_TRIANGLE_STRIP_ESP
    )

}