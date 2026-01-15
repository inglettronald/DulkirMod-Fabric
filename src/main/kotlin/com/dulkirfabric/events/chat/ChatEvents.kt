package com.dulkirfabric.events.chat

import com.dulkirfabric.events.base.CancellableEvent
import com.dulkirfabric.events.base.ModifyTextEvent
import net.minecraft.network.chat.Component

class ChatEvents {
    data class
    AllowChat(val message: Component) : CancellableEvent()

    data class
    ModifyChat(val message: Component): ModifyTextEvent(message)

}