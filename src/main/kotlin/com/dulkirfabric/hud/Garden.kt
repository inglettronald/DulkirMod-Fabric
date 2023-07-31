package com.dulkirfabric.hud

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.HudRenderEvent
import com.dulkirfabric.util.TablistUtils.persistentInfo
import meteordevelopment.orbit.EventHandler
import moe.nea.jarvis.api.Point
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object Garden {
    private val visitorHud = DulkirConfig.hudElement("visitors", Text.literal("Visitors"), 100, 21,
        Point(0.4056462738575835, 0.055456899963360104),1.101687f)
    private val composterHud = DulkirConfig.hudElement("composter", Text.literal("Composter"), 100, 21,
        Point(0.027783937063393563, 0.10514400299398007),0.9619154f)
    private val pitchYawHud = DulkirConfig.hudElement("pitch/yaw", Text.literal("Pitch/Yaw"), 100, 21,
        Point(0.027783937063393563, 0.15514400299398007),0.9619154f)

    @EventHandler
    fun onHudRender(event: HudRenderEvent) {
        if (persistentInfo.area != "Garden") return
        val context = event.context
        val matrices = context.matrices
        if (DulkirConfig.configOptions.visitorHud) {
            matrices.push()
            visitorHud.applyTransformations(matrices)

            val visitorText = Text.literal("Visitors: ")
                .setStyle(Style.EMPTY.withBold(true).withColor(Formatting.GREEN))
                .append(Text.literal(persistentInfo.numVisitors.toString())
                    .setStyle(Style.EMPTY.withColor(Formatting.GRAY).withBold(false)))
            context.drawText(mc.textRenderer, visitorText,0, 1, -1, true)
            val nextVisitor = Text.literal("Next: ")
                .setStyle(Style.EMPTY.withColor(Formatting.GOLD))
                .append(Text.literal(persistentInfo.nextVisitorTime)
                    .setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
            context.drawText(mc.textRenderer, nextVisitor,3, 11, -1, true)
            matrices.pop()
        }
        if (DulkirConfig.configOptions.showComposterInfo) {
            matrices.push()
            composterHud.applyTransformations(matrices)

            val composterText = Text.literal("Composter Time: ")
                .setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN))
                .append(Text.literal(persistentInfo.compostTime)
                    .setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
            context.drawText(mc.textRenderer, composterText,0, 1, -1, true)
            matrices.pop()
        }
        var yaw = mc.player?.yaw ?: return
        val pitch = mc.player?.pitch ?: return
        yaw %= 360f
        if (yaw < -180.0f) {
            yaw += 360.0f;
        } else if (yaw > 180.0f) {
            yaw -= 360.0f;
        }

        if (DulkirConfig.configOptions.pitchYawDisplay) {
            matrices.push()
            pitchYawHud.applyTransformations(matrices)

            val yawText = Text.literal("Yaw: ")
                .setStyle(Style.EMPTY.withColor(Formatting.GOLD))
                .append(Text.literal("%.2f".format(yaw))
                    .setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
            context.drawText(mc.textRenderer, yawText,0, 1, -1, true)
            val pitchText = Text.literal("Pitch: ")
                .setStyle(Style.EMPTY.withColor(Formatting.GOLD))
                .append(Text.literal("%.2f".format(pitch))
                    .setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
            context.drawText(mc.textRenderer, pitchText,0, 13, -1, true)
            matrices.pop()
        }
    }
}