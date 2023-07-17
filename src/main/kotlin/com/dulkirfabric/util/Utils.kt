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
        println("Path: ${event.sound.id.path}")
        println("Pitch: ${event.sound.pitch}")
        println("Volume: ${event.sound.volume}")
    }
}