package com.dulkirfabric.events.chat

import com.dulkirfabric.events.base.ModifyTextEvent
import net.minecraft.network.chat.Component

data class OverlayReceivedEvent(val message: Component): ModifyTextEvent(message)
