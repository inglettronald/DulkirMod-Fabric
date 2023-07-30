package com.dulkirfabric.events

import com.dulkirfabric.events.base.Event

class SlayerBossEvents {
    data class Spawn(
        val type: String,
        var timestamp: Long = System.currentTimeMillis()
    ): Event()

    data class Kill(
        val type: String,
        var timestamp: Long = System.currentTimeMillis()
    ): Event()

    data class Fail(
        val type: String?,
        var timestamp: Long = System.currentTimeMillis()
    ): Event()
}
