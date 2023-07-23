package com.dulkirfabric.events.base

import com.dulkirfabric.DulkirModFabric
import net.minecraft.text.Text

abstract class ModifyTextEvent(original: Text) {
    private var returnValue: Text = original

    fun setReturnValue(new: Text) {
        returnValue = new
    }
    fun post(): Text {
        return DulkirModFabric.EVENT_BUS.post(this).returnValue
    }
}