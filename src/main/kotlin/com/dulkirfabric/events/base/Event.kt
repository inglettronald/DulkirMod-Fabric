package com.dulkirfabric.events.base

import com.dulkirfabric.DulkirModFabric

abstract class Event {
    fun post() {
        DulkirModFabric.EVENT_BUS.post(this)
    }
}