package com.dulkirfabric.hud

import com.dulkirfabric.DulkirModFabric
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.HudRenderEvent
import com.dulkirfabric.util.ActionBarUtil
import com.dulkirfabric.util.Utils
import meteordevelopment.orbit.EventHandler
import net.minecraft.network.chat.Component
import org.joml.Vector2i

object ActionBarHudReplacements {
    private val hpHud = DulkirConfig.registerHud("hp_hud", Component.literal("Health"), 50, 11,
        Vector2i(0, 0), 1.2286775f)
    private val defHud = DulkirConfig.registerHud("def_hud", Component.literal("Def"), 50, 25,
        Vector2i(0, 0), 1.189786f)
    private val stackHud = DulkirConfig.registerHud("stack_hud", Component.literal("Stacks"), 50, 11,
        Vector2i(0, 0), 1.2318832f)
    private val manaHud = DulkirConfig.registerHud("mana_hud", Component.literal("Mana"), 50, 11,
        Vector2i(0, 0), 1.2286775f)

    @EventHandler
    fun onHudRender(event: HudRenderEvent) {
        if (!Utils.isInSkyblock()) return
        if (!DulkirConfig.configOptions.hudifyActionBar) return
        val context = event.context
        val pose = context.pose()
        pose.pushMatrix()
        hpHud.applyTransformations(pose)
        context.drawString(DulkirModFabric.mc.font, Component.literal(ActionBarUtil.healthStr),0, 1, -1, true)
        pose.popMatrix()

        pose.pushMatrix()
        defHud.applyTransformations(pose)
        context.drawString(DulkirModFabric.mc.font, Component.literal(ActionBarUtil.defStr),0, 1, -1, true)
        if (DulkirConfig.configOptions.showEHP) {
            context.drawString(DulkirModFabric.mc.font, Component.literal(ActionBarUtil.ehp), 0, 1 + 12, -1, true)
        }
        pose.popMatrix()

        pose.pushMatrix()
        stackHud.applyTransformations(pose)
        context.drawString(DulkirModFabric.mc.font, Component.literal(ActionBarUtil.stacks),0, 1, -1, true)
        pose.popMatrix()

        pose.pushMatrix()
        manaHud.applyTransformations(pose)
        context.drawString(DulkirModFabric.mc.font, Component.literal(ActionBarUtil.mana),0, 1, -1, true)
        pose.popMatrix()
    }
}