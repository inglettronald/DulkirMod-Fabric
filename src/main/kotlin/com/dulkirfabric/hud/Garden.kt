package com.dulkirfabric.hud

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.HudRenderEvent
import com.dulkirfabric.util.TablistUtils.persistentInfo
import meteordevelopment.orbit.EventHandler
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import org.joml.Vector2i

object Garden {
    private val visitorHud = DulkirConfig.registerHud("visitors", Component.literal("Visitors"), 100, 21,
        Vector2i(0, 0), 1.101687f)
    private val composterHud = DulkirConfig.registerHud("composter", Component.literal("Composter"), 100, 21,
        Vector2i(0, 0), 0.9619154f)
    private val pitchYawHud = DulkirConfig.registerHud("pitch_yaw", Component.literal("Pitch/Yaw"), 100, 21,
        Vector2i(0, 0), 0.9619154f)

    @EventHandler
    fun onHudRender(event: HudRenderEvent) {
        if (persistentInfo.area != "Garden") return
        val context = event.context
        val pose = context.pose()
        if (DulkirConfig.configOptions.visitorHud) {
            pose.pushMatrix()
            visitorHud.applyTransformations(pose)

            val visitorText = Component.literal("Visitors: ")
                .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)
                .append(Component.literal(persistentInfo.numVisitors.toString())
                    .withStyle(ChatFormatting.GRAY))
            context.drawString(mc.font, visitorText,0, 1, -1, true)
            val nextVisitor = Component.literal("Next: ")
                .withStyle(ChatFormatting.GOLD)
                .append(Component.literal(persistentInfo.nextVisitorTime)
                    .withStyle(ChatFormatting.GRAY))
            context.drawString(mc.font, nextVisitor,3, 11, -1, true)
            pose.popMatrix()
        }
        if (DulkirConfig.configOptions.showComposterInfo) {
            pose.pushMatrix()
            composterHud.applyTransformations(pose)

            val composterText = Component.literal("Composter Time: ")
                .withStyle(ChatFormatting.DARK_GREEN)
                .append(Component.literal(persistentInfo.compostTime)
                    .withStyle(ChatFormatting.GRAY))
            context.drawString(mc.font, composterText,0, 1, -1, true)
            pose.popMatrix()
        }
        var yaw = mc.player?.xRot ?: return
        val pitch = mc.player?.yRot ?: return
        yaw %= 360f
        if (yaw < -180.0f) {
            yaw += 360.0f;
        } else if (yaw > 180.0f) {
            yaw -= 360.0f;
        }

        if (DulkirConfig.configOptions.pitchYawDisplay) {
            pose.pushMatrix()
            pitchYawHud.applyTransformations(pose)

            val yawText = Component.literal("Yaw: ")
                .withStyle(ChatFormatting.GOLD)
                .append(Component.literal("%.2f".format(yaw))
                    .withStyle(ChatFormatting.GRAY))
            context.drawString(mc.font, yawText,0, 1, -1, true)
            val pitchText = Component.literal("Pitch: ")
                .withStyle(ChatFormatting.GOLD)
                .append(Component.literal("%.2f".format(pitch))
                    .withStyle(ChatFormatting.GRAY))
            context.drawString(mc.font, pitchText,0, 13, -1, true)
            pose.popMatrix()
        }
    }
}