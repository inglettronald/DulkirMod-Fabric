package com.dulkirfabric.events

import com.dulkirfabric.events.base.Event

data class AreaChangeEvent(val newArea: String, val prevArea: String): Event()