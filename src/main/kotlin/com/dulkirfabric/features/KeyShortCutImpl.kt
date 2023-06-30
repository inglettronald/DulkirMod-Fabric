package com.dulkirfabric.features

import com.dulkirfabric.commands.DynamicKeyCommand
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.WorldKeyPressEvent
import com.dulkirfabric.util.TextUtils
import meteordevelopment.orbit.EventHandler

object KeyShortCutImpl {
    private var lastCommandHandle: Long = 0
    private var prevCode: Int = 0;

    @EventHandler
    fun onKeyPress(event: WorldKeyPressEvent) {
        DulkirConfig.configOptions.macrosList.forEach {
            if (it.keyBinding.code == event.key) {
                // Spam Prevention
                if (event.key == prevCode && System.currentTimeMillis() - lastCommandHandle < 1000)
                    return

                lastCommandHandle = System.currentTimeMillis()
                prevCode = event.key

                TextUtils.sendCommand(it.command.trimStart('/'))
            }
        }

        if (DulkirConfig.configOptions.dynamicKey.code == event.key) {
            if (event.key == prevCode && System.currentTimeMillis() - lastCommandHandle < 1000)
                return
            lastCommandHandle = System.currentTimeMillis()
            prevCode = event.key
            TextUtils.sendCommand(DynamicKeyCommand.command.trimStart('/'))
        }
    }
}