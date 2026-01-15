package com.dulkirfabric.features

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.LongUpdateEvent
import com.dulkirfabric.util.TablistUtils
import com.dulkirfabric.util.Utils
import com.dulkirfabric.util.render.HudRenderUtil
import meteordevelopment.orbit.EventHandler
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import java.time.Duration
import kotlin.jvm.optionals.getOrNull


object BrokenHyp {

    private var oldKill = -1
    private var oldChampionXp = -1.0
    private var oldID = ""

    private var kill = -1
    private var championXp = -1.0
    private var id = ""

    @EventHandler
    fun onLong(event: LongUpdateEvent) {
        if (!DulkirConfig.configOptions.brokenHypNotif) return
        val stack: ItemStack = mc.player?.mainHandItem ?: return

        // get info about held item
        val tag = Utils.getNbt(stack) ?: return
        id = tag.getCompound("ExtraAttributes").getOrNull()?.getString("id")?.getOrNull() ?: ""

        kill = tag.getCompound("ExtraAttributes").getOrNull()?.getInt("stats_book")?.getOrNull() ?: -1
        championXp = tag.getCompound("ExtraAttributes").getOrNull()?.getDouble("champion_combat_xp")?.getOrNull() ?: -1.0

        // check if a wither blade, then check if same id
        if (!(id matches "(HYPERION|ASTRAEA|SCYLLA|VALKYRIE)".toRegex())) {
            return
        } else if (id != oldID) {
            // Check if this is a valid item for testing whether bestiary is broken.
            // That is, to be specific, check that it has champion and book of stats.
            // If it doesn't, don't reset because it can't be used anyway.
            if (kill == -1 || championXp == -1.0) {
                return
            }
            // If we get here this is a new item that is legitimate for testing bugged xp, in theory.
            oldID = id
            oldKill = kill
            oldChampionXp = championXp
            return
        }

        // If this section of the code is reached, then we have the same item, and we can check for updated stats
        if (oldKill != kill && oldChampionXp == championXp && TablistUtils.persistentInfo.area != "Private Island") {
            HudRenderUtil.drawTitle(Component.literal("Hype Broken").withStyle(ChatFormatting.RED),
                Duration.ofSeconds(2))
        }
        // update item regardless of whether it is bugged or not
        oldKill = kill
        oldChampionXp = championXp
    }
}