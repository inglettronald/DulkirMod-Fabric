package com.dulkirfabric.events

import com.dulkirfabric.events.base.Event
import net.minecraft.client.gui.GuiGraphics

data class HudRenderEvent(
    val context: GuiGraphics,
    val delta: Float
): Event()
