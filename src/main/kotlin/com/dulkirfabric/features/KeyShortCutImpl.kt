package com.dulkirfabric.features

import com.dulkirfabric.commands.DynamicKeyCommand
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.WorldKeyPressEvent
import com.dulkirfabric.util.TextUtils
import com.dulkirfabric.util.Utils
import meteordevelopment.orbit.EventHandler

object KeyShortCutImpl {
    private var lastCommandHandle: Long = 0
    private var prevCode: Int = 0;

    @EventHandler
    fun onKeyPress(event: WorldKeyPressEvent) {
        if (DulkirConfig.configOptions.macrosSkyBlockOnly && !Utils.isInSkyblock()) return
        DulkirConfig.configOptions.macrosList.forEach {
            if (it.keyBinding.code == event.key) {
                // Spam Prevention
                if (event.key == prevCode && System.currentTimeMillis() - lastCommandHandle < 1000)
                    return

                lastCommandHandle = System.currentTimeMillis()
                prevCode = event.key

                // This conditional allows for these shortcuts to work for commands or normal messages
                // You have to do it this way because the messages are handled differently on the client
                // side in modern versions of Minecraft.
                if (it.command.startsWith("/")) {
                    TextUtils.sendCommand(it.command.trimStart('/'))
                } else {
                    TextUtils.sendMessage(it.command)
                }
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