package com.dulkirfabric.util.render

import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.util.BufferAllocator

object RenderUtil {

    fun getBufferFor(multiphase: RenderLayer.MultiPhase): BufferBuilder {
        return BufferBuilder(
            BufferAllocator(multiphase.expectedBufferSize),
            multiphase.drawMode,
            multiphase.vertexFormat
        )
    }

}