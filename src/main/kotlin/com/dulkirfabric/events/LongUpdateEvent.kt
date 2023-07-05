package com.dulkirfabric.events

import com.dulkirfabric.events.base.Event

/**
 * Utility event for tasks that only run 1 time per second
 */
object LongUpdateEvent: Event()