package com.dulkirfabric.events

import com.dulkirfabric.events.base.Event
import net.minecraft.client.gui.screens.Screen

data class MouseScrollEvent(
    val screen: Screen,
    val mouseX: Double,
    val mouseY: Double,
    val horizontalScrollAmount: Double,
    val verticalScrollAmount: Double
): Event()
