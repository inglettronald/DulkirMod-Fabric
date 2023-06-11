package com.dulkirfabric.events

import com.dulkirfabric.events.base.Event
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext

data class
WorldRenderLastEvent(val context: WorldRenderContext): Event()
