package com.dulkirfabric.events.base

import com.dulkirfabric.DulkirModFabric
import meteordevelopment.orbit.ICancellable

abstract class CancellableEvent: ICancellable {

    var cancelled: Boolean = false

    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancelled: Boolean) {
        this.cancelled = cancelled
    }

    /**
     * Posts a given event to the bus and returns whether the user wishes to cancel it
     */
    fun post(): Boolean {
        DulkirModFabric.EVENT_BUS.post(this)
        return cancelled
    }
}