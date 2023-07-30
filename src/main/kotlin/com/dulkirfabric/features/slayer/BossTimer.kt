package com.dulkirfabric.features.slayer

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.SlayerBossEvents
import com.dulkirfabric.util.TextUtils
import meteordevelopment.orbit.EventHandler

object BossTimer {

    private var lastSpawnTime: Long = 0
    private var lastType = ""

    @EventHandler
    fun onSlayerStart(event: SlayerBossEvents.Spawn) {
        lastType = event.type
        lastSpawnTime = event.timestamp
    }

    @EventHandler
    fun onSlayerKill(event: SlayerBossEvents.Kill) {
        if (!DulkirConfig.configOptions.slayerKillTime) return
        if (lastType != event.type) return
        val bossTime: Float = (event.timestamp - lastSpawnTime) / 1000f
        TextUtils.info("§6Slayer Boss took ${"%.2f".format(bossTime)}s to kill.")
    }
}