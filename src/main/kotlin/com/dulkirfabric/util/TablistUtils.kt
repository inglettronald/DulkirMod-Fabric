package com.dulkirfabric.util

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.events.AreaChangeEvent
import com.dulkirfabric.events.LongUpdateEvent
import meteordevelopment.orbit.EventHandler
import net.minecraft.client.network.PlayerListEntry

object TablistUtils {
    var tablist: List<PlayerListEntry>? = null
    private val areaPattern = "Area: (.+)".toRegex()
    private val speedPattern = "^Speed: (.+)".toRegex()
    private val visitorPattern = "Visitors: \\((.+)\\)".toRegex()
    private val nextVisitorPattern = "Next Visitor: (.+)".toRegex()
    private val compostTimePattern = "Time Left: (.+)".toRegex()

    data class PersistentInfo(
        var area: String = "",
        var speed: String = "",
        var numVisitors: Int = 0,
        var nextVisitorTime: String = "",
        var compostTime: String = ""
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
            val str = it.displayName?.string?.trim() ?: return@forEach
            areaPattern.find(str)?.let { result ->
                if (persistentInfo.area != result.groupValues[1]) {
                    AreaChangeEvent(result.groupValues[1], persistentInfo.area).post()
                    persistentInfo.area = result.groupValues[1]
                }
                return@forEach
            }

            speedPattern.matchEntire(str)?.let { result ->
                persistentInfo.speed = result.groupValues[1]
                return@forEach
            }

            visitorPattern.matchEntire(str)?.let { result ->
                persistentInfo.numVisitors = result.groupValues[1].toInt()
                return@forEach
            }

            nextVisitorPattern.matchEntire(str)?.let { result ->
                persistentInfo.nextVisitorTime = result.groupValues[1]
                return@forEach
            }

            compostTimePattern.matchEntire(str)?.let { result ->
                persistentInfo.compostTime = result.groupValues[1]
                return@forEach
            }
        }
    }
}