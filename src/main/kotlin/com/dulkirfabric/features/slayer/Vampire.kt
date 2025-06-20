package com.dulkirfabric.features.slayer

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.SlayerBossEvents
import com.dulkirfabric.events.WorldRenderLastEvent
import com.dulkirfabric.util.ScoreBoardUtils
import com.dulkirfabric.util.TablistUtils
import com.dulkirfabric.util.Utils.getInterpolatedPos
import com.dulkirfabric.util.Utils.getSkullTexture
import com.dulkirfabric.util.render.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.item.Items
import net.minecraft.util.math.Box
import java.awt.Color

object Vampire {

    private const val char = "҉"
    private const val ichorTexture =
        "ewogICJ0aW1lc3RhbXAiIDogMTYxNTg4ODAwMDU1MywKICAicHJvZmlsZUlkIiA6ICI5ZDIyZGRhOTVmZGI0MjFmOGZhNjAzNTI1YThkZmE4ZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTYWZlRHJpZnQ0OCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jMDM0MDkyM2E2ZGU0ODI1YTE3NjgxM2QxMzM1MDNlZmYxODZkYjA4OTZlMzJiNjcwNDkyOGMyYTJiZjY4NDIyIgogICAgfQogIH0KfQ="
    private val box = Box(-.4, .1, -.4, .4, -1.9, .4)
    private var inT5 = false
    private val ichorBox = Box(- .5, 1.0, -.5, .5, 2.0, .5,)

    @EventHandler
    fun steakDisplay(event: WorldRenderLastEvent) {
        if (!DulkirConfig.configOptions.steakDisplay) return
        if (TablistUtils.persistentInfo.area != "The Rift") return

        val ents = mc.world?.entities ?: return
        ents.forEach {
            if (it !is ArmorStandEntity || !it.hasCustomName()) return@forEach
            if (!it.isMarker) return@forEach
            if (!it.isCustomNameVisible) return@forEach
            val name = it.customName?.string ?: return@forEach
            if (name.contains(char) && name.contains("Bloodfiend")) {
                val pos = it.getInterpolatedPos(mc.renderTickCounter.getTickProgress(true))
                WorldRenderUtils.drawWireFrame(
                    event.context,
                    box.offset(pos.x, pos.y, pos.z),
                    Color(0, 255, 255),
                    8f
                )
            }
        }
    }

    @EventHandler
    fun ichorHighlight(event: WorldRenderLastEvent) {
        if (!inT5) return
        if (!DulkirConfig.configOptions.ichorHighlight) return
        if (TablistUtils.persistentInfo.area != "The Rift") return

        val ents = mc.world?.entities ?: return
        ents.forEach {
            if (it !is ArmorStandEntity) return@forEach
            val itemStack = it.getEquippedStack(EquipmentSlot.HEAD) ?: return@forEach
            val item = itemStack.item ?: return@forEach
            if (item !== Items.PLAYER_HEAD) return@forEach
            val textureId = getSkullTexture(itemStack) ?: return@forEach
            if (textureId != ichorTexture) return@forEach
            val pos = it.getInterpolatedPos(mc.renderTickCounter.getTickProgress(true))
            WorldRenderUtils.drawWireFrame(
                event.context,
                ichorBox.offset(pos.x, pos.y, pos.z),
                Color(0, 255, 255),
                8f,
            )
        }
    }

    @EventHandler
    fun onSlayerStart(event: SlayerBossEvents.Spawn) {
        if (ScoreBoardUtils.slayerType == "Riftstalker Bloodfiend V")
            inT5 = true
    }

    @EventHandler
    fun onSlayerKill(event: SlayerBossEvents.Kill) {
        inT5 = false
    }

    @EventHandler
    fun onSlayerFail(event: SlayerBossEvents.Fail) {
        inT5 = false
    }
}