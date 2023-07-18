package com.dulkirfabric.features

import com.dulkirfabric.events.EntityLoadEvent
import com.dulkirfabric.events.WorldRenderLastEvent
import com.dulkirfabric.util.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import java.awt.Color

object RenderTest {

    @EventHandler
    fun onRender(event: WorldRenderLastEvent) {
        WorldRenderUtils.renderWaypoint(
            Text.literal("Home Base").setStyle(Style.EMPTY.withColor(Color(255, 100, 150, 255).rgb)), event.context,
            Vec3d(-183.5, 79.0, -465.5)
        )

//        mc.world?.entities?.forEach {
//            if (it is GlowingEntityInterface)
//                it.setDulkirEntityGlow(true, Color(255, 255, 255, 255), Random(it.id).nextBoolean())
//        }
    }

    @EventHandler
    fun onLoadEnt(event: EntityLoadEvent) {
//        if (event.entity !is GlowingEntityInterface) return
//        event.entity.setDulkirEntityGlow(true, Color(255, 255, 255, 255),false)
    }
}