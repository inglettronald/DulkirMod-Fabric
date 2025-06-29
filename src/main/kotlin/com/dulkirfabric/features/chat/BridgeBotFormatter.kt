package com.dulkirfabric.features.chat

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.chat.ChatEvents
import com.dulkirfabric.util.TextUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.text.Style
import net.minecraft.text.Text

object BridgeBotFormatter {
    private val bridgeRegex: Regex =
        "^(?:§r|)(§2Guild|§3Officer) > (?:\\S+ )?([\\w§]{3,18})(?: §[a-z0-9]\\[[\\w]+])?§f: ([^»>:]+)( » | > |: ).+".toRegex()

    @EventHandler
    fun onChat(event: ChatEvents.ModifyChat) {
        if (!DulkirConfig.configOptions.bridgeFormatter) return

        val message = event.message.string
        if (bridgeRegex matches message) {
            val matchResult = bridgeRegex.find(message)
            val (prefix, name, playerName) = matchResult!!.destructured
            if (TextUtils.stripColorCodes(name).equals(DulkirConfig.configOptions.bridgeBotName, ignoreCase = true)) {
                val newPrefix = if (prefix == "§2Guild") "§2Bridge" else "§3Bridge"
                val newMessage = event.message.copy()
                newMessage.siblings[0] = Text.literal("$newPrefix > ")
                        .append(Text.literal(playerName).setStyle(Style.EMPTY.withColor(DulkirConfig.configOptions.bridgeNameColor)))
                        .append(": ")
                newMessage.siblings[1] = Text.literal(event.message.siblings[1].string.replace("$playerName > ", ""))
                        .setStyle(event.message.siblings[1].style)

                event.setReturnValue(newMessage)
            }
        }
    }
}