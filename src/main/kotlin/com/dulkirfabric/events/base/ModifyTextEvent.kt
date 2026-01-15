package com.dulkirfabric.events.base

import com.dulkirfabric.DulkirModFabric
import net.minecraft.network.chat.Component

abstract class ModifyTextEvent(original: Component) {
    private var returnValue: Component = original

    fun setReturnValue(new: Component) {
        returnValue = new
    }
    fun post(): Component {
        return DulkirModFabric.EVENT_BUS.post(this).returnValue
    }
}