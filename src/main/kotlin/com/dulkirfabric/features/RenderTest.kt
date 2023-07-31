package com.dulkirfabric.features

import com.dulkirfabric.events.EntityLoadEvent
import com.dulkirfabric.events.WorldRenderLastEvent
import com.dulkirfabric.util.render.GlowingEntityInterface
import com.dulkirfabric.util.render.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import java.awt.Color

object RenderTest {

//    @EventHandler
//    fun onRender(event: WorldRenderLastEvent) {
//        WorldRenderUtils.renderWaypoint(
//            Text.literal("Home Base").setStyle(Style.EMPTY.withColor(Color(255, 100, 150, 255).rgb)), event.context,
//            Vec3d(-183.5, 79.0, -465.5)
//        )
//
//        WorldRenderUtils.drawBox(event.context, 16.0, 119.0, -6.0, 2.0, 2.0, 2.0, Color(0, 200, 200, 100), false)
//    }
//
//    @EventHandler
//    fun onLoadEnt(event: EntityLoadEvent) {
//        //if (event.entity !is GlowingEntityInterface) return
//        //event.entity.setDulkirEntityGlow(true, Color(0, 0, 255, 255))
//    }
}