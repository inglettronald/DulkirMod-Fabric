package com.dulkirfabric.features

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.WorldRenderLastEvent
import com.dulkirfabric.events.chat.ChatReceivedEvent
import com.dulkirfabric.util.TablistUtils
import com.dulkirfabric.util.TextUtils
import com.dulkirfabric.util.TextUtils.unformattedString
import com.dulkirfabric.util.render.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.Vec3d

object ArachneFeatures {

    private val keeperWaypoints: Set<POI> = setOf(
        POI(Vec3d(-208.5, 44.5, -259.5), Text.literal("1").setStyle(Style.EMPTY.withColor(Formatting.GOLD))),
        POI(Vec3d(-311.5, 43.5, -232.5), Text.literal("2").setStyle(Style.EMPTY.withColor(Formatting.GOLD))),
        POI(Vec3d(-230.5, 57.5, -307.5), Text.literal("3").setStyle(Style.EMPTY.withColor(Formatting.GOLD))),
        POI(Vec3d(-269.5, 47.5, -166.5), Text.literal("4").setStyle(Style.EMPTY.withColor(Formatting.GOLD))),
        POI(Vec3d(-292.5, 47.5, -167.5), Text.literal("5").setStyle(Style.EMPTY.withColor(Formatting.GOLD))),
        POI(Vec3d(-291.5, 47.5, -183.5), Text.literal("6").setStyle(Style.EMPTY.withColor(Formatting.GOLD))),
        POI(Vec3d(-282.5, 47.5, -195.5), Text.literal("7").setStyle(Style.EMPTY.withColor(Formatting.GOLD))),
        POI(Vec3d(-262.5, 49.5, -191.5), Text.literal("8").setStyle(Style.EMPTY.withColor(Formatting.GOLD))),
        POI(Vec3d(-269.5, 61.5, -159.5), Text.literal("9").setStyle(Style.EMPTY.withColor(Formatting.GOLD)))
    )

    data class POI(val pos: Vec3d, val name: Text)

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
    fun onChat(event: ChatReceivedEvent) {
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
        if (spawnmillis <=startmillis) return

        var time: Int = if (bigboy) {
            (40 - (System.currentTimeMillis() - spawnmillis) / 1000).toInt()
        } else {
            (18 - (System.currentTimeMillis() - spawnmillis) / 1000).toInt()
        }
        if (time < 0) time = 0
        WorldRenderUtils.drawText(Text.literal(time.toString()).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)),
            event.context, Vec3d(-282.5, 50.8, -178.5))
    }

}