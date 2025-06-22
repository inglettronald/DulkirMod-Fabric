package com.dulkirfabric.events

import com.dulkirfabric.events.base.Event

/**
 * Should only be called when the minecraft screen is null (i.e: just in the gameplay world, not chat or inventory etc
 */
data class WorldKeyPressEvent(val key: Int, val scancode: Int, val modifiers: Int): Event()