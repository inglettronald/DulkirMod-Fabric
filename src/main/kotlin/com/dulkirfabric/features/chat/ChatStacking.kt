package com.dulkirfabric.features.chat

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.events.LongUpdateEvent
import com.dulkirfabric.events.chat.ChatEvents
import com.dulkirfabric.util.ScoreBoardUtils.formattedString
import meteordevelopment.orbit.EventHandler
import net.minecraft.text.Text

object ChatStacking {

    data class ChatLog(var timestamp: Long, val message: Text, var frequency: Int)

    private var chatLogs = mutableSetOf<ChatLog>()

    @EventHandler
    fun modifyChat(event: ChatEvents.ModifyChat) {
        chatLogs.forEach { curMsg ->
            if (curMsg.message.string.equals(event.message.string)) {
                curMsg.timestamp = System.currentTimeMillis()
                curMsg.frequency++
                mc.inGameHud.chatHud.messages.removeIf { msg ->
                    msg.content.string == curMsg.message.string
                            && mc.inGameHud.ticks - msg.creationTick >= 1200
                }
                mc.inGameHud.chatHud.refresh()
                event.setReturnValue(Text.literal("${curMsg.message.formattedString()} §7(${curMsg  .frequency})"))
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