package com.dulkirfabric.features

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.chat.ModifyCommandEvent
import meteordevelopment.orbit.EventHandler

object AliasImpl {
    @EventHandler
    fun onCommand(event: ModifyCommandEvent) {
        DulkirConfig.configOptions.aliasList.forEach {
            if (it.alias == event.command) {
                event.command = it.command
            }
        }
    }
}