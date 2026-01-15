package com.dulkirfabric.events

import com.dulkirfabric.events.base.CancellableEvent
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext
import net.minecraft.client.renderer.state.BlockOutlineRenderState

data class BlockOutlineEvent(
    val worldRenderContext: WorldRenderContext,
    val blockOutlineContext: BlockOutlineRenderState
): CancellableEvent()
