package com.dulkirfabric.util.render

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.render.VertexFormats

object DulkirRenderPipelines {

    val DULKIR_LINES: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(*arrayOf<RenderPipeline.Snippet?>(RenderPipelines.RENDERTYPE_LINES_SNIPPET))
            .withLocation("pipeline/line_strip")
            /*.withVertexShader("core/position_color_normal")*/
            /*.withFragmentShader("core/position_color_normal")*/
            .withVertexFormat(VertexFormats.POSITION_COLOR_NORMAL, DrawMode.LINE_STRIP)
            .withCull(false)
            .withoutBlend()
            .withDepthWrite(true)
            .build()
    )

    val DULKIR_LINES_ESP: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(*arrayOf<RenderPipeline.Snippet?>(RenderPipelines.RENDERTYPE_LINES_SNIPPET))
            .withLocation("pipeline/line_strip")
            /*.withVertexShader("core/position_color_normal")*/
            /*.withFragmentShader("core/position_color_normal")*/
            .withShaderDefine("shad")
            .withVertexFormat(VertexFormats.POSITION_COLOR_NORMAL, DrawMode.LINE_STRIP)
            .withCull(false)
            .withoutBlend()
            .withDepthWrite(false)
            .build()
    )

    val DULKIR_TEXT: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(
            *arrayOf<RenderPipeline.Snippet?>(
                RenderPipelines.TEXT_SNIPPET,
                RenderPipelines.FOG_SNIPPET
            )
        )
            .withLocation("pipeline/text")
            .withVertexShader("core/rendertype_text")
            .withFragmentShader("core/rendertype_text")
            .withSampler("Sampler0")
            .withSampler("Sampler2")
            .withoutBlend()
            .withCull(false)
            .withDepthWrite(true)
            .build()
    )

    val DULKIR_TEXT_ESP: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(
            *arrayOf<RenderPipeline.Snippet?>(
                RenderPipelines.TEXT_SNIPPET,
                RenderPipelines.FOG_SNIPPET
            )
        )
            .withLocation("pipeline/text")
            .withVertexShader("core/rendertype_text")
            .withFragmentShader("core/rendertype_text")
            .withSampler("Sampler0")
            .withSampler("Sampler2")
            .withoutBlend()
            .withCull(false)
            .withDepthWrite(false)
            .build()
    )

    val DULKIR_TRIANGLE_STRIP: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(*arrayOf<RenderPipeline.Snippet?>(RenderPipelines.POSITION_COLOR_SNIPPET))
            .withLocation("pipeline/debug_filled_box")
            .withCull(false)
            .withVertexFormat(VertexFormats.POSITION_COLOR, DrawMode.TRIANGLE_STRIP)
            .withDepthWrite(true)
            .withoutBlend()
            .build()
    );

    val DULKIR_TRIANGLE_STRIP_ESP: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(*arrayOf<RenderPipeline.Snippet?>(RenderPipelines.POSITION_COLOR_SNIPPET))
            .withLocation("pipeline/debug_filled_box")
            .withCull(false)
            .withVertexFormat(VertexFormats.POSITION_COLOR, DrawMode.TRIANGLE_STRIP)
            .withDepthWrite(false)
            .withoutBlend()
            .build()
    );


}