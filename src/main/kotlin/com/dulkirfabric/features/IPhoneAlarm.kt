package com.dulkirfabric.features

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.ClientTickEvent
import com.dulkirfabric.util.SoundUtil
import com.dulkirfabric.util.TimeMark
import meteordevelopment.orbit.EventHandler
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.core.BlockPos
import java.time.Duration

object IPhoneAlarm {
    private var lastPosition: BlockPos? = null
    private var lastMoved = TimeMark.farPast()
    private val soundInstances = mutableListOf<SoundInstance>()

    @EventHandler
    fun onTick(event: ClientTickEvent) {
        if (DulkirConfig.configOptions.alarmTimeout <= 0) return
        val p = mc.player ?: return
        val lastPosition = this.lastPosition
        this.lastPosition = p.blockPosition()
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