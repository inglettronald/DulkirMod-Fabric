package com.dulkirfabric.features

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.WorldRenderLastEvent
import com.dulkirfabric.events.chat.ChatEvents
import com.dulkirfabric.util.TablistUtils
import com.dulkirfabric.util.TextUtils
import com.dulkirfabric.util.TextUtils.unformattedString
import com.dulkirfabric.util.render.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3

object ArachneFeatures {

    private val keeperWaypoints: Set<POI> = setOf(
        POI(Vec3(-208.5, 44.5, -259.5), "1"),
        POI(Vec3(-311.5, 43.5, -232.5), "2"),
        POI(Vec3(-230.5, 57.5, -307.5), "3"),
        POI(Vec3(-269.5, 47.5, -166.5), "4"),
        POI(Vec3(-292.5, 47.5, -167.5), "5"),
        POI(Vec3(-291.5, 47.5, -183.5), "6"),
        POI(Vec3(-282.5, 47.5, -195.5), "7"),
        POI(Vec3(-262.5, 49.5, -191.5), "8"),
        POI(Vec3(-269.5, 61.5, -159.5), "9")
    )

    private class POI {
        val pos: Vec3
        val name: Component

        constructor(pos: Vec3, name: String) {
            this.pos = pos
            this.name = Component.literal(name).withStyle(ChatFormatting.GOLD)
        }
    }

    private val spawnRegex = "^\\[BOSS] Arachne: (?<tier>(With your sacrifice.)|(A befitting welcome!))$".toRegex()

    private var start: Long = -1
    private var end: Long = -1
    private var spawn: Long = -1
    private var bigboy: Boolean = false

    @EventHandler
    fun onRenderWorldLast(event: WorldRenderLastEvent) {
        if (TablistUtils.persistentInfo.area != "Spider's Den") return
        if (!DulkirConfig.configOptions.arachneKeeperWaypoints) return
        keeperWaypoints.forEach {
            WorldRenderUtils.renderWaypoint(it.name, event.context, it.pos)
        }
    }

    @EventHandler
    fun onChat(event: ChatEvents.AllowChat) {
        if (!arachneTimerEnabled()) return
        val str = event.message.unformattedString.trim()
        if (str matches spawnRegex) {
            bigboy = false
            start = System.currentTimeMillis()
        } else if (str.startsWith('☄') && str.contains("Something is awakening!")) {
            if (str.contains("Arachne Crystal!")) bigboy = true
            spawn = System.currentTimeMillis()
        } else if (str.startsWith("ARACHNE DOWN!")) {
            end = System.currentTimeMillis()
            if (start > -1) {
                val killtime = (end - start).toFloat() / 1000
                TextUtils.info("§6Arachne took §7$killtime §6seconds to kill.")
            }
        }
    }

    @EventHandler
    fun onWorldRenderLast(event: WorldRenderLastEvent) {
        if (!shouldDisplayTimer()) return

        var time: Int = if (bigboy) {
            (40 - (System.currentTimeMillis() - spawn) / 1000).toInt()
        } else {
            (18 - (System.currentTimeMillis() - spawn) / 1000).toInt()
        }
        if (time < 0) time = 0
        WorldRenderUtils.renderWaypoint(
            Component.literal(time.toString()).withStyle(ChatFormatting.LIGHT_PURPLE),
            event.context, Vec3(-282.5, 50.8, -178.5), false
        )
    }

    private fun shouldDisplayTimer(): Boolean {
        return arachneTimerEnabled()
                && spawn > start
                && spawn > end
    }

    private fun arachneTimerEnabled(): Boolean {
        return DulkirConfig.configOptions.arachneSpawnTimer
                && TablistUtils.persistentInfo.area == "Spider's Den"
    }

}