package com.dulkirfabric.features

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.WorldKeyPressEvent
import com.dulkirfabric.util.TextUtils
import meteordevelopment.orbit.EventHandler

object KeyShortCutImpl {
    @EventHandler
    fun onKeyPress(event: WorldKeyPressEvent) {
        DulkirConfig.configOptions.macrosList.forEach {
            if (it.keyBinding.code == event.key) {
                // This conditional allows for these shortcuts to work for commands or normal messages
                if (it.command.startsWith("/"))
                    TextUtils.sendCommand(it.command.substring(1))
                else
                    TextUtils.sendMessage(it.command)
            }
        }
    }
}