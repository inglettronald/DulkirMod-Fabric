package com.dulkirfabric.events

import com.dulkirfabric.events.base.Event

data class
WorldKeyPressEvent(val key: Int, val scancode: Int, val modifiers: Int): Event()