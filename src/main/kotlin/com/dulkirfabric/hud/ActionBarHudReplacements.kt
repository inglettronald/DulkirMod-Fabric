package com.dulkirfabric.hud

import com.dulkirfabric.DulkirModFabric
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.HudRenderEvent
import com.dulkirfabric.util.ActionBarUtil
import com.dulkirfabric.util.Utils
import meteordevelopment.orbit.EventHandler
import net.minecraft.text.Text
import org.joml.Vector2i

object ActionBarHudReplacements {
    private val hpHud = DulkirConfig.registerHud("hpHud", Text.literal("Health"), 50, 11,
        Vector2i(0, 0), 1.2286775f)
    private val defHud = DulkirConfig.registerHud("defHud", Text.literal("Def"), 50, 25,
        Vector2i(0, 0), 1.189786f)
    private val stackHud = DulkirConfig.registerHud("stackHud", Text.literal("Stacks"), 50, 11,
        Vector2i(0, 0), 1.2318832f)
    private val manaHud = DulkirConfig.registerHud("manaHud", Text.literal("Mana"), 50, 11,
        Vector2i(0, 0), 1.2286775f)

    @EventHandler
    fun onHudRender(event: HudRenderEvent) {
        if (!Utils.isInSkyblock()) return
        if (!DulkirConfig.configOptions.hudifyActionBar) return
        val context = event.context
        val matrices = context.matrices
        matrices.pushMatrix()
        hpHud.applyTransformations(matrices)
        context.drawText(DulkirModFabric.mc.textRenderer, Text.literal(ActionBarUtil.healthStr),0, 1, -1, true)
        matrices.popMatrix()

        matrices.pushMatrix()
        defHud.applyTransformations(matrices)
        context.drawText(DulkirModFabric.mc.textRenderer, Text.literal(ActionBarUtil.defStr),0, 1, -1, true)
        if (DulkirConfig.configOptions.showEHP) {
            context.drawText(DulkirModFabric.mc.textRenderer, Text.literal(ActionBarUtil.ehp), 0, 1 + 12, -1, true)
        }
        matrices.popMatrix()

        matrices.pushMatrix()
        stackHud.applyTransformations(matrices)
        context.drawText(DulkirModFabric.mc.textRenderer, Text.literal(ActionBarUtil.stacks),0, 1, -1, true)
        matrices.popMatrix()

        matrices.pushMatrix()
        manaHud.applyTransformations(matrices)
        context.drawText(DulkirModFabric.mc.textRenderer, Text.literal(ActionBarUtil.mana),0, 1, -1, true)
        matrices.popMatrix()
    }
}