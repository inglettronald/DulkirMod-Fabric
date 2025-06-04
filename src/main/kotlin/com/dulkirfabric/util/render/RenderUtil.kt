package com.dulkirfabric.util.render

import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.util.BufferAllocator

object RenderUtil {

    fun getBufferFor(layer: RenderLayer): BufferBuilder {
        return BufferBuilder(
            BufferAllocator(layer.expectedBufferSize),
            layer.pipeline.vertexFormatMode,
            layer.pipeline.vertexFormat
        )
    }

}