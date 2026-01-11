package com.dulkirfabric.events

import com.dulkirfabric.events.base.CancellableEvent
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext
import net.minecraft.client.render.state.OutlineRenderState

data class BlockOutlineEvent(
    val worldRenderContext: WorldRenderContext,
    val blockOutlineContext: OutlineRenderState
): CancellableEvent()
