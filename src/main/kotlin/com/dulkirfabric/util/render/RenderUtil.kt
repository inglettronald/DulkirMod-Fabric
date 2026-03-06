package com.dulkirfabric.util.render

import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.ByteBufferBuilder
import net.minecraft.client.renderer.rendertype.RenderType

object RenderUtil {

    fun getBufferFor(renderType: RenderType): BufferBuilder {
        return BufferBuilder(
            ByteBufferBuilder(renderType.bufferSize()),
            renderType.mode(),
            renderType.format()
        )
    }

}