package com.dulkirfabric.events

import com.dulkirfabric.events.base.CancellableEvent
import net.minecraft.client.gui.DrawContext
import net.minecraft.screen.slot.Slot

interface SlotRenderEvent {
    val context: DrawContext
    val slot: Slot
    val mouseX: Int
    val mouseY: Int
    val delta: Float

    data class Before(
        override val context: DrawContext, override val slot: Slot,
        override val mouseX: Int,
        override val mouseY: Int,
        override val delta: Float
    ) : CancellableEvent(),
        SlotRenderEvent

    data class After(
        override val context: DrawContext, override val slot: Slot,
        override val mouseX: Int,
        override val mouseY: Int,
        override val delta: Float
    ) : CancellableEvent(),
        SlotRenderEvent
}