package com.dulkirfabric.hud

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.HudRenderEvent
import com.dulkirfabric.util.TablistUtils
import meteordevelopment.orbit.EventHandler
import moe.nea.jarvis.api.Point
import net.minecraft.text.Text

object SpeedOverlay {
    private val speedHud = DulkirConfig.hudElement("SpeedHud", Text.literal("Speed"), 24 + 4, 11,
        Point(0.3669986675110943, 0.9857798862704453), 1.6367052f)

    @EventHandler
    fun onHudRender(event: HudRenderEvent) {
        val context = event.context
        val matrices = context.matrices
        matrices.push()
        speedHud.applyTransformations(matrices)
        context.drawText(mc.textRenderer, Text.literal(TablistUtils.persistentInfo.speed),0, 1, -1, true)
        matrices.pop()
    }
}