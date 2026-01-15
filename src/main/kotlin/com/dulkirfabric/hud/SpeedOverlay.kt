package com.dulkirfabric.hud

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.ClientTickEvent
import com.dulkirfabric.events.HudRenderEvent
import com.dulkirfabric.util.TablistUtils
import com.dulkirfabric.util.Utils
import meteordevelopment.orbit.EventHandler
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3
import org.joml.Vector2i

object SpeedOverlay {

    private val speedHud = DulkirConfig.registerHud("speed_hud", Component.literal("Speed"), 24 + 4, 11,
        Vector2i(0,0), 1.6367052f)
    private val bpsOverlay = DulkirConfig.registerHud("movement_hud", Component.literal("Speed (BPS)"), 69, 11,
        Vector2i(0,0), 1.5052f)

    private var tickMomentum = 0.0

    @EventHandler
    fun onHudRender(event: HudRenderEvent) {
        if (!Utils.isInSkyblock()) return
        val context = event.context
        val pose = context.pose()
        if (DulkirConfig.configOptions.speedHud) {
            pose.pushMatrix()
            speedHud.applyTransformations(pose)
            context.drawString(mc.font, Component.literal(TablistUtils.persistentInfo.speed),0, 1, -1, true)
            pose.popMatrix()
        }
        if (DulkirConfig.configOptions.speedBpsHud) {
            pose.pushMatrix()
            bpsOverlay.applyTransformations(pose)
            context.drawString(mc.font, Component.literal("${"%.2f".format(tickMomentum)} BPS (speed)"),0, 1, -1, true)
            pose.popMatrix()
        }
    }

    @EventHandler
    fun onTick(event: ClientTickEvent) {
        val player = mc.player ?: return
        val last = Vec3(player.xo, player.yo, player.zo)
        val now = player.position() ?: return
        tickMomentum = last.distanceTo(now) * 20
    }
}