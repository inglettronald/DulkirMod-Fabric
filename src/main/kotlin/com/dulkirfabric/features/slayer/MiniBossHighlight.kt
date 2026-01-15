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
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.phys.AABB
import java.awt.Color
import java.time.Duration


object MiniBossHighlight {
    data class MiniBoss(val name: String, val box: AABB)

    private val zombieBox = AABB(-.4, .1, -.4, .4, -1.9, .4)
    private val spiderBox = AABB(-.7, -.3, -.7, .7, -1.2, .7)
    private val wolfBox = AABB(-.4, -0.0, -.4, .4, -.85, .4)
    private val emanBox = AABB(-.4, 0.0, -.4, .4, -2.9, .4)
    private val blazeBox = AABB(-.4, -.2, -.4, .4, -2.0, .4)

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

        val ents = mc.level?.entitiesForRendering() ?: return

        ents.forEach {
            if (it !is ArmorStand) return@forEach
            val name = it.customName?.string ?: return@forEach
            val result = miniBosses.find { mini -> name.contains(mini.name) } ?: return@forEach
            WorldRenderUtils.drawWireFrame(
                    event.context,
                    result.box.move(it.getInterpolatedPos(mc.deltaTracker.getGameTimeDeltaPartialTick(true))),
                    Color(0, 255, 0),
                    8f
            )
        }
    }

    // TODO: fix, we can use chat messages now afaik?
    @EventHandler
    fun onSound(event: PlaySoundEvent) {
        if (!DulkirConfig.configOptions.announceMinis) return
        if (event.sound.location.path != "entity.generic.explode") return
        if (event.sound.pitch != 1.2857143f) return
        if (event.sound.volume != .6f) return
        HudRenderUtil.drawTitle(Component.literal("MiniBoss Spawned").withStyle(ChatFormatting.RED),
            Duration.ofMillis(1000))
    }
}