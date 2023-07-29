package com.dulkirfabric.features.chat

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.events.LongUpdateEvent
import com.dulkirfabric.events.chat.ChatEvents
import com.dulkirfabric.util.ScoreBoardUtils.formattedString
import com.dulkirfabric.util.TextUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.text.MutableText
import net.minecraft.text.Text

object ChatStacking {

    data class ChatLog(var timestamp: Long, val message: Text, var frequency: Int)

    private var chatLogs = mutableSetOf<ChatLog>()
    private val stackRegex = """ §7\((\d+)\)$""".toRegex()
    private val whiteListedMessages = listOf(
        "".toRegex(),
        "(-+)".toRegex()
    )

    @EventHandler
    fun modifyChat(event: ChatEvents.AllowChat) {
        chatLogs.forEach { curMsg ->
            if (curMsg.message.string.equals(event.message.string)) {
                // Probably don't stack empty lines and the long bars
                if (whiteListedMessages.any {
                    it matches event.message.string
                    }) return

                curMsg.timestamp = System.currentTimeMillis()
                curMsg.frequency++
                mc.inGameHud.chatHud.messages.removeIf { msg ->
                    TextUtils.stripColorCodes(msg.content.string.replace(stackRegex, "")) == curMsg.message.string
                            && mc.inGameHud.ticks - msg.creationTick <= 1200
                }
                mc.inGameHud.chatHud.addMessage(Text.literal("${curMsg.message.formattedString()} §7(${curMsg.frequency})"))
                mc.inGameHud.chatHud.refresh()
                // TODO: implement something like this to avoid stripColorCodes call
                // MutableText.of(event.message.content).append(Text.literal(" §7(${curMsg.frequency})"))
                event.cancel()
                return
            }
        }
        chatLogs.add(ChatLog(System.currentTimeMillis(), event.message, 1))
    }

    @EventHandler
    fun cullLogs(event: LongUpdateEvent) {
        chatLogs.removeIf {
            System.currentTimeMillis() - it.timestamp >= 60000
        }
    }
}