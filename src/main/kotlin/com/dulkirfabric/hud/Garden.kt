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
        Point(0.4056462738575835, 0.4479382203757649),1.2286775f)
    private val composterHud = DulkirConfig.hudElement("composter", Text.literal("Composter"), 100, 21,
        Point(0.4056462738575835, 0.4479382203757649),1.2286775f)

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
    }
}