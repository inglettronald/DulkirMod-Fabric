package com.dulkirfabric.util

import com.dulkirfabric.events.PlaySoundEvent

object Utils {
    fun isInSkyblock(): Boolean {
        return ScoreBoardUtils.getLines() != null
    }

    /**
     * Prints relevant information about a sound that is being displayed
     */
    fun debugSound(event: PlaySoundEvent) {
        if (event.sound.id.path == "entity.player.hurt"
            && event.sound.pitch == 0f
            && event.sound.volume == 0f) return
        println("Path: ${event.sound.id.path}")
        println("Pitch: ${event.sound.pitch}")
        println("Volume: ${event.sound.volume}")
    }
}