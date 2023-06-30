package com.dulkirfabric.events.chat

import com.dulkirfabric.events.base.CancellableEvent
import net.minecraft.text.Text

data class
ChatReceivedEvent(val message: Text): CancellableEvent()