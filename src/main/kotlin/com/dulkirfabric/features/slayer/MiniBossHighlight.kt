package com.dulkirfabric.features.slayer

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.PlaySoundEvent
import com.dulkirfabric.events.WorldRenderLastEvent
import com.dulkirfabric.util.ScoreBoardUtils
import com.dulkirfabric.util.Utils.getInterpolatedPos
import com.dulkirfabric.util.render.HudRenderUtil
import com.dulkirfabric.util.render.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.Box
import java.awt.Color
import java.time.Duration


object MiniBossHighlight {
    data class MiniBoss(val name: String, val box: Box)

    private val zombieBox = Box(-.4, .1, -.4, .4, -1.9, .4)
    private val spiderBox = Box(-.7, -.3, -.7, .7, -1.2, .7)
    private val wolfBox = Box(-.4, -0.0, -.4, .4, -.85, .4)
    private val emanBox = Box(-.4, 0.0, -.4, .4, -2.9, .4)
    private val blazeBox = Box(-.4, -.2, -.4, .4, -2.0, .4)

    private val miniBosses = listOf(
        MiniBoss("Revenant Sycophant", zombieBox),
        MiniBoss("Revenant Champion", zombieBox),
        MiniBoss("Deformed Revenant", zombieBox),
        MiniBoss("Atoned Champion", zombieBox),
        MiniBoss("Atoned Revenant", zombieBox),
        MiniBoss("Tarantula Vermin", spiderBox),
        MiniBoss("Tarantula Beast", spiderBox),
        MiniBoss("Mutant Tarantula", spiderBox),
        MiniBoss("Pack Enforcer", wolfBox),
        MiniBoss("Sven Follower", wolfBox),
        MiniBoss("Sven Alpha", wolfBox),
        MiniBoss("Voidling Devotee", emanBox),
        MiniBoss("Voidling Radical", emanBox),
        MiniBoss("Voidcrazed Maniac", emanBox),
        MiniBoss("Flare Demon", blazeBox),
        MiniBoss("Kindleheart Demon", blazeBox),
        MiniBoss("Burningsoul Demon", blazeBox)
    )

    @EventHandler
    fun drawMiniBossBoxes(event: WorldRenderLastEvent) {
        if (!ScoreBoardUtils.hasActiveSlayerQuest) return
        if (!DulkirConfig.configOptions.boxMinis) return

        val ents = mc.world?.entities ?: return

        ents.forEach {
            if (it !is ArmorStandEntity) return@forEach
            val name = it.customName?.string ?: return@forEach
            val result = miniBosses.find { mini -> name.contains(mini.name) } ?: return@forEach
            WorldRenderUtils.drawWireFrame(event.context, result.box.offset(it.getInterpolatedPos(event.context.tickDelta()))
                , Color(0, 255, 0), 8f)
        }
    }

    // TODO: fix
    @EventHandler
    fun onSound(event: PlaySoundEvent) {
        if (!DulkirConfig.configOptions.announceMinis) return
        if (event.sound.id.path != "entity.generic.explode") return
        if (event.sound.pitch != 1.2857143f) return
        if (event.sound.volume != .6f) return
        HudRenderUtil.drawTitle(Text.literal("MiniBoss Spawned").setStyle(Style.EMPTY.withColor(Formatting.RED)),
            Duration.ofMillis(1000))
    }
}