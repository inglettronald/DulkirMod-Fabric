package com.dulkirfabric.features

import com.dulkirfabric.events.WorldRenderLastEvent
import com.dulkirfabric.util.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.util.math.Box
import java.awt.Color

object RenderBoxTest {

    @EventHandler
    fun onRender(event: WorldRenderLastEvent) {
        WorldRenderUtils.drawBox(
            event.context, Box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
            Color(255, 255, 255, 255), 3f, true
        )
    }
}