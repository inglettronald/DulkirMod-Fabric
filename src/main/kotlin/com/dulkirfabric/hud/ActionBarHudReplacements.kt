package com.dulkirfabric.hud

import com.dulkirfabric.DulkirModFabric
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.HudRenderEvent
import com.dulkirfabric.util.ActionBarUtil
import com.dulkirfabric.util.Utils
import meteordevelopment.orbit.EventHandler
import moe.nea.jarvis.api.Point
import net.minecraft.text.Text

object ActionBarHudReplacements {
    private val hpHud = DulkirConfig.hudElement("hpHud", Text.literal("Health"), 50, 11, Point(0.21, 0.21))
    private val defHud = DulkirConfig.hudElement("defHud", Text.literal("Def"), 50, 25, Point(0.22, 0.22))
    private val stackHud = DulkirConfig.hudElement("stackHud", Text.literal("Stacks"), 50, 11, Point(0.22, 0.22))
    private val manaHud = DulkirConfig.hudElement("manaHud", Text.literal("Mana"), 50, 11, Point(0.23, 0.23))

    @EventHandler
    fun onHudRender(event: HudRenderEvent) {
        if (!Utils.isInSkyblock()) return
        if (!DulkirConfig.configOptions.hudifyActionBar) return
        val context = event.context
        val matrices = context.matrices
        matrices.push()
        hpHud.applyTransformations(matrices)
        context.drawText(DulkirModFabric.mc.textRenderer, Text.literal(ActionBarUtil.healthStr),0, 1, -1, true)
        matrices.pop()

        matrices.push()
        defHud.applyTransformations(matrices)
        context.drawText(DulkirModFabric.mc.textRenderer, Text.literal(ActionBarUtil.defStr),0, 1, -1, true)
        if (DulkirConfig.configOptions.showEHP) {
            context.drawText(DulkirModFabric.mc.textRenderer, Text.literal(ActionBarUtil.ehp), 0, 1 + 12, -1, true)
        }
        matrices.pop()

        matrices.push()
        stackHud.applyTransformations(matrices)
        context.drawText(DulkirModFabric.mc.textRenderer, Text.literal(ActionBarUtil.stacks),0, 1, -1, true)
        matrices.pop()

        matrices.push()
        manaHud.applyTransformations(matrices)
        context.drawText(DulkirModFabric.mc.textRenderer, Text.literal(ActionBarUtil.mana),0, 1, -1, true)
        matrices.pop()
    }
}