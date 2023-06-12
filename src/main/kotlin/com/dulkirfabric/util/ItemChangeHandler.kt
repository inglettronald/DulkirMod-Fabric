package com.dulkirfabric.util

import com.dulkirfabric.events.TooltipRenderChangeEvent

object ItemChangeHandler {
    var prevName = ""
    fun handle(name: String) {
        if (name != prevName) {
            prevName = name
            TooltipRenderChangeEvent.post()
        }
    }
}