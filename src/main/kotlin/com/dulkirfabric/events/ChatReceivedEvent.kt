package com.dulkirfabric.events

import com.dulkirfabric.events.base.CancellableEvent
import net.minecraft.text.Text

data class
ChatReceivedEvent(val message: Text): CancellableEvent()