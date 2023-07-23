package com.dulkirfabric.util.render

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