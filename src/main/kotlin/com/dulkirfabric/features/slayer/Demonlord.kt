package com.dulkirfabric.features.slayer

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.events.PlaySoundEvent
import com.dulkirfabric.events.WorldRenderLastEvent
import com.dulkirfabric.util.TablistUtils
import com.dulkirfabric.util.TextUtils
import com.dulkirfabric.util.Utils.debugSound
import com.dulkirfabric.util.Utils.getInterpolatedPos
import com.dulkirfabric.util.render.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.util.math.Box
import java.awt.Color

object Demonlord {
    private val phaseColors = listOf(
        "CRYSTAL ♨" to Color(15, 247, 236, 255),
        "ASHEN ♨" to Color(0, 0, 0, 255),
        "AURIC ♨" to Color(206, 219, 57, 255),
        "SPIRIT ♨" to Color(255, 255, 255, 255)
    )
    private val box = Box(-.5, -.4, -.5, .5, -1.9, .5)

    @EventHandler
    fun attunementHighlight(event: WorldRenderLastEvent) {
        if (TablistUtils.persistentInfo.area != "Crimson Isle") return
        val ents = mc.world?.entities ?: return
        ents.forEach { ent ->
            if (ent is ArmorStandEntity && ent.hasCustomName()) {
                val name = TextUtils.stripColorCodes(ent.customName?.string ?: return@forEach)
                val color = phaseColors.firstOrNull { name.contains(it.first) }?.second ?: return@forEach
                val pos = ent.getInterpolatedPos(event.context.tickDelta())
                WorldRenderUtils.drawWireFrame(
                    event.context,
                    box.offset(pos.x, pos.y, pos.z),
                    color,
                    8f
                )
            }
        }
    }
}