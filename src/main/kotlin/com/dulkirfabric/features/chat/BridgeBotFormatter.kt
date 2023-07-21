package com.dulkirfabric.features.chat

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.chat.ChatReceivedEvent
import com.dulkirfabric.util.TextUtils
import com.dulkirfabric.util.Utils
import meteordevelopment.orbit.EventHandler
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object BridgeBotFormatter {
    private val bridgeRegex: Regex = "^(§2Guild|§3Officer) > (?:\\S+ )?([\\w§]{3,18})(?: §[a-z0-9][[A-Z]+])?§f: ([^>]+)(?: >| »|:) (.+)".toRegex()

    @EventHandler
    fun onChat(event: ChatReceivedEvent) {
        if (!DulkirConfig.configOptions.bridgeFormatter) return

        val message = event.message.string
        if (bridgeRegex matches message) {
            val matchResult = bridgeRegex.find(message)
            val (prefix, name, playerName) = matchResult!!.destructured
            if (TextUtils.stripColorCodes(name).equals(DulkirConfig.configOptions.bridgeBotName, ignoreCase = true)) {
                val newPrefix = if (prefix == "§2Guild") "§2Bridge" else "§3Bridge"
                event.message.siblings[0] = Text.literal(
                    "$newPrefix > "
                ).append(Text.literal(playerName).setStyle(Style.EMPTY.withColor(DulkirConfig.configOptions.bridgeNameColor)))
                    .append(": ")
                event.message.siblings[1] = Text.literal(
                    event.message.siblings[1].string.replace("$playerName > ", "")
                ).setStyle(event.message.siblings[1].style)
            }
        }
    }
}