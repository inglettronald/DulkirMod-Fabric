package com.dulkirfabric.events

import com.dulkirfabric.events.base.CancellableEvent

data class MousePressEvent(val code: Int) : CancellableEvent()