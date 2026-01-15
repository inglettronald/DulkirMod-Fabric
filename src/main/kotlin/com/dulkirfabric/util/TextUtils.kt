package com.dulkirfabric.util

import com.dulkirfabric.DulkirModFabric.mc
import net.minecraft.network.chat.Component

object TextUtils {
    const val CHAT_PREFIX = "§f<§3DulkirMod§f>§r"
    private val colorRegex = "§.".toRegex()
    fun info(text: String, prefix: Boolean = true) {
        if (mc.player == null) return

        val textPrefix = if (prefix) "${CHAT_PREFIX} " else ""
        mc.gui.chat.addMessage(Component.literal("$textPrefix$text§r"))
    }

    fun toggledMessage(message: String, state: Boolean) {
        val stateText = if (state) "§aON" else "§cOFF"
        info("§9Toggled $message §8[$stateText§8]§r")
    }

    val Component.unformattedString
        get() = string.replace("§.".toRegex(), "")

    fun sendPartyChatMessage(message: String) {
        this.sendCommand("/pc $message")
    }

    fun sendMessage(message: String) {
        mc.player?.connection?.sendChat(message)
    }

    fun sendCommand(command: String) {
        mc.player?.connection?.sendCommand(command)
    }

    fun stripColorCodes(string: String): String {
        return string.replace(colorRegex, "")
    }
}