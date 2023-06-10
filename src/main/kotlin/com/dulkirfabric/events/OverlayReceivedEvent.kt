package com.dulkirfabric.events

import com.dulkirfabric.events.base.CancellableEvent

data class OverlayReceivedEvent(val message: String): CancellableEvent()
