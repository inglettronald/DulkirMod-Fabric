package com.dulkirfabric.events

import com.dulkirfabric.events.base.Event
import net.minecraft.client.gui.DrawContext

data class HudRenderEvent(
    val context: DrawContext,
    val delta: Float
): Event()
