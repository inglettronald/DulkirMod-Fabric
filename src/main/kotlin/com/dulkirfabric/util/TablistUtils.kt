package com.dulkirfabric.util

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.events.LongUpdateEvent
import meteordevelopment.orbit.EventHandler
import net.minecraft.client.network.PlayerListEntry

object TablistUtils {
    var tablist: List<PlayerListEntry>? = null
    private val areaPattern = "Area: (.+)".toRegex()
    var area: String = ""

    @EventHandler
    fun onLongUpdate(event: LongUpdateEvent) {
        if (mc.player == null) return
        tablist = mc.inGameHud.playerListHud.collectPlayerEntries()
        area = updateArea()
    }

    private fun updateArea(): String {
        if (tablist == null) return ""
        tablist!!.forEach {
            val match = areaPattern.find(it.displayName?.string ?: return@forEach) ?: return@forEach
            return match.groupValues[1]
        }
        return ""
    }
}