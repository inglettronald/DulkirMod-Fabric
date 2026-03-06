package com.dulkirfabric.util.render

import net.minecraft.client.renderer.rendertype.LayeringTransform
import net.minecraft.client.renderer.rendertype.RenderSetup
import net.minecraft.client.renderer.rendertype.RenderType

object DulkirRenderTypes {

    val DULKIR_LINES: RenderType = RenderType.create(
        "dulkir_lines",
        RenderSetup.builder(DulkirRenderPipelines.DULKIR_LINES)
            .bufferSize(RenderType.TRANSIENT_BUFFER_SIZE)
            .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
            .createRenderSetup()
    )

    val DULKIR_LINES_ESP: RenderType  = RenderType.create(
        "dulkir_lines_esp",
        RenderSetup.builder(DulkirRenderPipelines.DULKIR_LINES_ESP)
            .bufferSize(RenderType.TRANSIENT_BUFFER_SIZE)
            .createRenderSetup()
    )

    val DULKIR_TEXT: RenderType  = RenderType.create(
        "dulkir_text",
        RenderSetup.builder(DulkirRenderPipelines.DULKIR_TEXT)
            .bufferSize(RenderType.TRANSIENT_BUFFER_SIZE)
            .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
            .createRenderSetup()
    )

    val DULKIR_TEXT_ESP: RenderType  = RenderType.create(
        "dulkir_text_esp",
        RenderSetup.builder(DulkirRenderPipelines.DULKIR_TEXT_ESP)
            .bufferSize(RenderType.TRANSIENT_BUFFER_SIZE)
            .createRenderSetup()
    )

    val DULKIR_TRIANGLE_STRIP: RenderType  = RenderType.create(
        "dulkir_triangle_strip",
        RenderSetup.builder(DulkirRenderPipelines.DULKIR_TRIANGLE_STRIP)
            .bufferSize(RenderType.TRANSIENT_BUFFER_SIZE)
            .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
            .sortOnUpload()
            .createRenderSetup()
    )

    val DULKIR_TRIANGLE_STRIP_ESP: RenderType = RenderType.create(
        "dulkir_triangle_strip_esp",
        RenderSetup.builder(DulkirRenderPipelines.DULKIR_TRIANGLE_STRIP_ESP)
            .bufferSize(RenderType.TRANSIENT_BUFFER_SIZE)
            .sortOnUpload()
            .createRenderSetup()
    )

    val DULKIR_QUADS: RenderType = RenderType.create(
        "dulkir_quads",
        RenderSetup.builder(DulkirRenderPipelines.DULKIR_QUADS_ESP)
            .bufferSize(RenderType.TRANSIENT_BUFFER_SIZE)
            .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
            .sortOnUpload()
            .createRenderSetup()
    )

    val DULKIR_QUADS_ESP: RenderType = RenderType.create(
        "dulkir_quads_esp",
        RenderSetup.builder(DulkirRenderPipelines.DULKIR_QUADS_ESP)
            .bufferSize(RenderType.TRANSIENT_BUFFER_SIZE)
            .sortOnUpload()
            .createRenderSetup()
    )

    val TYPES: Set<RenderType> = setOf(
        DULKIR_LINES,
        DULKIR_LINES_ESP,
        DULKIR_TEXT,
        DULKIR_TEXT_ESP,
        DULKIR_TRIANGLE_STRIP,
        DULKIR_TRIANGLE_STRIP_ESP,
        DULKIR_QUADS,
        DULKIR_QUADS_ESP
    )

}