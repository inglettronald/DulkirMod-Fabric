package com.dulkirfabric.events.chat

import com.dulkirfabric.events.base.CancellableEvent
import com.dulkirfabric.events.base.ModifyTextEvent
import net.minecraft.text.Text

class ChatEvents {
    data class
    AllowChat(val message: Text) : CancellableEvent()

    data class
    ModifyChat(val message: Text): ModifyTextEvent(message)

}