package com.dulkirfabric.events.chat

import com.dulkirfabric.events.base.CancellableEvent

data class OverlayReceivedEvent(val message: String): CancellableEvent()
