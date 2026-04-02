package com.dulkirfabric.util.render

import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.ByteBufferBuilder
import net.minecraft.client.renderer.rendertype.RenderType

object RenderUtil {

    private val buffers = mutableMapOf<RenderType, BufferBuilder>()

    fun getBufferFor(renderType: RenderType): BufferBuilder {
        val buffer = buffers.getOrPut(renderType) {
            BufferBuilder(
                ByteBufferBuilder(renderType.bufferSize()),
                renderType.mode(),
                renderType.format()
            )
        }
        return buffer
    }

    fun endFrame() {
        buffers.clear()
    }

}