package com.dulkirfabric.util.render

import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.ByteBufferBuilder
import net.minecraft.client.renderer.RenderType

object RenderUtil {

    fun getBufferFor(multiphase: RenderType.CompositeRenderType): BufferBuilder {
        return BufferBuilder(
            ByteBufferBuilder(multiphase.bufferSize()),
            multiphase.mode(),
            multiphase.format()
        )
    }

}