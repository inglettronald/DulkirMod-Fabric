package com.dulkirfabric.hud

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.ClientTickEvent
import com.dulkirfabric.events.HudRenderEvent
import com.dulkirfabric.util.TablistUtils
import meteordevelopment.orbit.EventHandler
import moe.nea.jarvis.api.Point
import net.fabricmc.loader.impl.lib.sat4j.core.Vec
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d

object SpeedOverlay {
    private val speedHud = DulkirConfig.hudElement("SpeedHud", Text.literal("Speed"), 24 + 4, 11,
        Point(0.028240898890913812, 0.9857798862704453), 1.6367052f)
    private val bpsOverlay = DulkirConfig.hudElement("MovementHud", Text.literal("Speed (BPS)"), 69, 11,
        Point(0.020240898890, 0.9857798862704453), 1.5052f)


    private var tickMomentum = 0.0

    @EventHandler
    fun onHudRender(event: HudRenderEvent) {
        val context = event.context
        val matrices = context.matrices
        if (DulkirConfig.configOptions.speedHud) {
            matrices.push()
            speedHud.applyTransformations(matrices)
            context.drawText(mc.textRenderer, Text.literal(TablistUtils.persistentInfo.speed),0, 1, -1, true)
            matrices.pop()
        }
        if (DulkirConfig.configOptions.speedBpsHud) {
            matrices.push()
            bpsOverlay.applyTransformations(matrices)
            context.drawText(mc.textRenderer, Text.literal("${"%.2f".format(tickMomentum)} BPS (speed)"),0, 1, -1, true)
            matrices.pop()
        }
    }

    @EventHandler
    fun onTick(event: ClientTickEvent) {
        val player = mc.player ?: return
        val last = Vec3d(player.prevX, player.prevY, player.prevZ)
        val now = player.pos ?: return
        tickMomentum = last.distanceTo(now) * 20
    }
}