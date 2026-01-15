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
        POI(Vec3(-208.5, 44.5, -259.5), Component.literal("1").withStyle(ChatFormatting.GOLD)),
        POI(Vec3(-311.5, 43.5, -232.5), Component.literal("2").withStyle(ChatFormatting.GOLD)),
        POI(Vec3(-230.5, 57.5, -307.5), Component.literal("3").withStyle(ChatFormatting.GOLD)),
        POI(Vec3(-269.5, 47.5, -166.5), Component.literal("4").withStyle(ChatFormatting.GOLD)),
        POI(Vec3(-292.5, 47.5, -167.5), Component.literal("5").withStyle(ChatFormatting.GOLD)),
        POI(Vec3(-291.5, 47.5, -183.5), Component.literal("6").withStyle(ChatFormatting.GOLD)),
        POI(Vec3(-282.5, 47.5, -195.5), Component.literal("7").withStyle(ChatFormatting.GOLD)),
        POI(Vec3(-262.5, 49.5, -191.5), Component.literal("8").withStyle(ChatFormatting.GOLD)),
        POI(Vec3(-269.5, 61.5, -159.5), Component.literal("9").withStyle(ChatFormatting.GOLD))
    )

    data class POI(val pos: Vec3, val name: Component)

    private val spawnRegex = "\\[BOSS] Arachne: (With your sacrifice.)|(A befitting welcome!)".toRegex()

    private var startmillis: Long = -1
    private var endmillis: Long = -1
    private var spawnmillis: Long = -1
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
        if (!DulkirConfig.configOptions.arachneSpawnTimer) return
        if (TablistUtils.persistentInfo.area != "Spider's Den") return
        val str = event.message.unformattedString.trim()
        if (str matches spawnRegex) {
            bigboy = false
            startmillis = System.currentTimeMillis()
        } else if (str.startsWith('☄') && str.contains("Something is awakening!")) {
            if (str.contains("Arachne Crystal!")) bigboy = true
            spawnmillis = System.currentTimeMillis()
        } else if (str.startsWith("Runecrafting:")) {
            endmillis = System.currentTimeMillis()
            if (startmillis > -1) {
                val killtime = (endmillis - startmillis).toFloat() / 1000
                TextUtils.info("§6Arachne took §7$killtime §6seconds to kill.")
            }
        }
    }

    @EventHandler
    fun onWorldRenderLast(event: WorldRenderLastEvent) {
        if (!DulkirConfig.configOptions.arachneSpawnTimer) return
        if (TablistUtils.persistentInfo.area != "Spider's Den") return
        if (spawnmillis <= startmillis) return

        var time: Int = if (bigboy) {
            (40 - (System.currentTimeMillis() - spawnmillis) / 1000).toInt()
        } else {
            (18 - (System.currentTimeMillis() - spawnmillis) / 1000).toInt()
        }
        if (time < 0) time = 0
        WorldRenderUtils.drawText(Component.literal(time.toString()).withStyle(ChatFormatting.LIGHT_PURPLE),
            event.context, Vec3(-282.5, 50.8, -178.5))
    }

}