package com.dulkirfabric.events

import com.dulkirfabric.events.base.CancellableEvent
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext.BlockOutlineContext

data class BlockOutlineEvent(
    val worldRenderContext: WorldRenderContext,
    val blockOutlineContext: BlockOutlineContext
): CancellableEvent()
