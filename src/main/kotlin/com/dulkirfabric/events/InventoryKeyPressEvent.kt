package com.dulkirfabric.events

import com.dulkirfabric.events.base.CancellableEvent

data class
InventoryKeyPressEvent(val keyCode: Int, val scanCode: Int, val modifiers: Int): CancellableEvent()