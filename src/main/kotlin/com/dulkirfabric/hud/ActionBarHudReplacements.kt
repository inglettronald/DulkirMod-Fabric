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
    private val hpHud = DulkirConfig.hudElement("hpHud", Text.literal("Health"), 50, 11,
        Point(0.4056462738575835, 0.8479382203757649),1.2286775f)
    private val defHud = DulkirConfig.hudElement("defHud", Text.literal("Def"), 50, 25,
        Point(0.6407475196141946, 0.9963439393439096), 1.189786f)
    private val stackHud = DulkirConfig.hudElement("stackHud", Text.literal("Stacks"), 50, 11,
        Point(0.4046055614026583, 0.8128084113198316), 1.2318832f)
    private val manaHud = DulkirConfig.hudElement("manaHud", Text.literal("Mana"), 50, 11,
        Point(0.5699228055607499, 0.8479382203757649), 1.2286775f)

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