package com.dulkirfabric.features

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.ClientTickEvent
import com.dulkirfabric.util.SoundUtil
import com.dulkirfabric.util.TimeMark
import meteordevelopment.orbit.EventHandler
import net.minecraft.client.sound.SoundInstance
import net.minecraft.util.math.BlockPos
import java.time.Duration

object IPhoneAlarm {
    var lastPosition: BlockPos? = null
    var lastMoved = TimeMark.farPast()
    val soundInstances = mutableListOf<SoundInstance>()

    @EventHandler
    fun onTick(event: ClientTickEvent) {
        if (DulkirConfig.configOptions.alarmTimeout <= 0) return
        val p = mc.player ?: return
        val lastPosition = this.lastPosition
        this.lastPosition = p.blockPos
        if (lastPosition != this.lastPosition) {
            lastMoved = TimeMark.now()
            SoundUtil.stop(soundInstances)
            soundInstances.clear()
        } else {
            if (lastMoved.timePassed() > Duration.ofSeconds(DulkirConfig.configOptions.alarmTimeout.toLong())) {
                soundInstances.add(SoundUtil.playIPhoneAlarm())
                lastMoved = TimeMark.ago(Duration.ofSeconds(4L))
            }
        }
    }

}