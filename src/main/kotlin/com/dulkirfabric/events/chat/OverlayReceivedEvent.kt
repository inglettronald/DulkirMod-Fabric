package com.dulkirfabric.events.chat

import com.dulkirfabric.events.base.ModifyTextEvent
import net.minecraft.text.Text

data class OverlayReceivedEvent(val message: Text): ModifyTextEvent(message)
