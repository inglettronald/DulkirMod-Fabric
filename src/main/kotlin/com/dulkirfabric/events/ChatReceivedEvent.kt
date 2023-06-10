package com.dulkirfabric.events

import com.dulkirfabric.events.base.CancellableEvent

data class
ChatReceivedEvent(val message: String): CancellableEvent()