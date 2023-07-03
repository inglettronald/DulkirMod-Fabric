package com.dulkirfabric.features

import com.dulkirfabric.events.WorldRenderLastEvent
import com.dulkirfabric.util.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.text.Text
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import java.awt.Color

object RenderBoxTest {

    @EventHandler
    fun onRender(event: WorldRenderLastEvent) {

        WorldRenderUtils.drawBox(
            event.context, Box(-183.0, 78.0, -465.0, -182.0, 79.0, -464.0),
            Color(255, 100, 150, 255), 10f, false
        )

        WorldRenderUtils.renderTextWithDistance(Text.literal("Hello worldgpqy"), event.context, event.context.consumers()!!,
            Vec3d(2.0, 0.0, 0.0), false,
            depthTest = false
        )
    }
}