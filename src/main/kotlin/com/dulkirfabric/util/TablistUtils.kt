package com.dulkirfabric.util

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.events.LongUpdateEvent
import meteordevelopment.orbit.EventHandler
import net.minecraft.client.network.PlayerListEntry

object TablistUtils {
    var tablist: List<PlayerListEntry>? = null
    private val areaPattern = "Area: (.+)".toRegex()
    private val speedPattern = "^Speed: (.+)".toRegex()
    data class PersistentInfo(
        var area: String = "",
        var speed: String = ""
    )

    var persistentInfo: PersistentInfo = PersistentInfo()

    @EventHandler
    fun onLongUpdate(event: LongUpdateEvent) {
        if (mc.player == null) return
        tablist = mc.inGameHud.playerListHud.collectPlayerEntries()
        updatePersistentData()
    }

    private fun updatePersistentData() {
        if (tablist == null) return
        tablist!!.forEach {
            areaPattern.find(it.displayName?.string ?: return@forEach) ?.let { result ->
                persistentInfo.area = result.groupValues[1]
                return@forEach
            }

            speedPattern.matchEntire(it.displayName?.string?.trim() ?: return@forEach) ?.let { result ->
                persistentInfo.speed = result.groupValues[1]
                return@forEach
            }

        }

    }
}