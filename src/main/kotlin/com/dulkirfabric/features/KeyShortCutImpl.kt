package com.dulkirfabric.features

import com.dulkirfabric.DulkirModFabric
import com.dulkirfabric.commands.DynamicKeyCommand
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.MousePressEvent
import com.dulkirfabric.events.WorldKeyPressEvent
import com.dulkirfabric.util.TextUtils
import com.dulkirfabric.util.Utils
import meteordevelopment.orbit.EventHandler

object KeyShortCutImpl {
    private var lastCommandHandle: Long = 0
    private var prevCode: Int = 0;

    @EventHandler
    fun onKeyPress(event: WorldKeyPressEvent) {
        handle(event.key)
    }

    @EventHandler
    fun onMouse(event: MousePressEvent) {
        val screen = DulkirModFabric.mc.currentScreen
        if (screen != null) return
        handle(event.code)
    }

    fun handle(code: Int) {
        if (DulkirConfig.configOptions.macrosSkyBlockOnly && !Utils.isInSkyblock()) return
        DulkirConfig.configOptions.macrosList.forEach {
            if (it.keyBinding.code == code) {
                // Spam Prevention
                if (code == prevCode && System.currentTimeMillis() - lastCommandHandle < 1000)
                    return

                lastCommandHandle = System.currentTimeMillis()
                prevCode = code

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

        if (DulkirConfig.configOptions.dynamicKey.code == code) {
            if (code == prevCode && System.currentTimeMillis() - lastCommandHandle < 1000)
                return
            lastCommandHandle = System.currentTimeMillis()
            prevCode = code
            TextUtils.sendCommand(DynamicKeyCommand.command.trimStart('/'))
        }
    }
}