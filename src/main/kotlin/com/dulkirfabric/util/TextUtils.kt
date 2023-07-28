package com.dulkirfabric.util

import com.dulkirfabric.DulkirModFabric.mc
import net.minecraft.text.Text

object TextUtils {
    const val CHAT_PREFIX = "§f<§3DulkirMod§f>§r"
    private val colorRegex = "§.".toRegex()
    fun info(text: String, prefix: Boolean = true) {
        if (mc.player == null) return

        val textPrefix = if (prefix) "${CHAT_PREFIX} " else ""
        mc.inGameHud.chatHud.addMessage(Text.literal("$textPrefix$text§r"))
    }

    fun toggledMessage(message: String, state: Boolean) {
        val stateText = if (state) "§aON" else "§cOFF"
        info("§9Toggled $message §8[$stateText§8]§r")
    }

    val Text.unformattedString
        get() = string.replace("§.".toRegex(), "")

    fun sendPartyChatMessage(message: String) {
        this.sendCommand("/pc $message")
    }

    fun sendMessage(message: String) {
        mc.player?.networkHandler?.sendChatMessage(message)
    }

    fun sendCommand(command: String) {
        mc.player?.networkHandler?.sendChatCommand(command)
    }

    fun stripColorCodes(string: String): String {
        return string.replace(colorRegex, "")
    }
}