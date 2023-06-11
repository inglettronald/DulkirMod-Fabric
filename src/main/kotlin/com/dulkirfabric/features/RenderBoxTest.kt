package com.dulkirfabric.features

import com.dulkirfabric.events.WorldRenderLastEvent
import com.dulkirfabric.util.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import java.awt.Color

object RenderBoxTest {

    @EventHandler
    fun onRender(event: WorldRenderLastEvent) {
        WorldRenderUtils.drawBox(event.context,0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            Color(255, 255, 255, 255), 3f, false)
    }
}