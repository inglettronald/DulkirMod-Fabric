package com.dulkirfabric.features

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.EntityLoadEvent
import com.dulkirfabric.events.HudRenderEvent
import com.dulkirfabric.events.WorldRenderLastEvent
import com.dulkirfabric.util.render.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import moe.nea.jarvis.api.Point
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
        //HudRenderUtil.drawTitle(event.context, Text.literal("Hello World!"))
    }

    @EventHandler
    fun onLoadEnt(event: EntityLoadEvent) {
        //if (event.entity !is GlowingEntityInterface) return
        //event.entity.setDulkirEntityGlow(true, Color(0, 0, 255, 255),false)
    }

    val fooHud = DulkirConfig.hudElement("foobar", Text.literal("Text"), 200, 10, Point(0.4, 0.4))

    @EventHandler
    fun onRenderHud(event: HudRenderEvent) {
        val context = event.context
        val matrices = context.matrices
        matrices.push()
        fooHud.applyTransformations(matrices)
        context.drawText(mc.textRenderer, Text.literal("§6This is the actual element."),0, 0, -1, true)
        matrices.pop()
    }
}