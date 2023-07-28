package com.dulkirfabric.features.slayer

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.events.WorldRenderLastEvent
import com.dulkirfabric.util.ScoreBoardUtils
import com.dulkirfabric.util.TextUtils
import com.dulkirfabric.util.Utils.getInterpolatedPos
import com.dulkirfabric.util.render.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.util.math.Box
import java.awt.Color


object MiniBossHighlight {
    data class MiniBoss(val name: String, val box: Box)

    private val zombieBox = Box(-.3, .1, -.3, .3, -1.9, .3)
    private val spiderBox = Box(-.7, -.3, -.7, .7, -1.2, .7)
    private val wolfBox = Box(-.3, -0.0, -.3, .3, -.85, .3)
    private val emanBox = Box(-.3, 0.0, -.3, .3, -2.9, .3)
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
        // TODO: check for a slayer quest being active
        if (!ScoreBoardUtils.hasActiveSlayerQuest) return

        val ents = mc.world?.entities ?: return

        ents.forEach {
            if (it !is ArmorStandEntity) return@forEach
            val name = it.customName?.string ?: return@forEach
            val result = miniBosses.find { mini -> name.contains(mini.name) } ?: return@forEach
            WorldRenderUtils.drawWireFrame(event.context, result.box.offset(it.getInterpolatedPos(event.context.tickDelta()))
                , Color(0, 255, 0), 8f)
        }
    }

}