package com.dulkirfabric.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.sound.SoundInstance
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.LocalRandom

object SoundUtil {
    val iphone = SoundEvent.of(Identifier("dulkirmod:iphone"))


    fun stop(sounds: List<SoundInstance>) {
        val m = MinecraftClient.getInstance().soundManager
        sounds.forEach(m::stop)
    }

    fun playSoundAtPlayer(event: SoundEvent): PositionedSoundInstance {
        val instance = PositionedSoundInstance(
            event,
            SoundCategory.MASTER,
            1F,
            1F,
            LocalRandom(0L),
            MinecraftClient.getInstance().player?.blockPos ?: BlockPos(0, 0, 0)
        )
        MinecraftClient.getInstance().soundManager.play(instance)
        return instance
    }

    fun playIPhoneAlarm(): PositionedSoundInstance {
        return playSoundAtPlayer(iphone)
    }
}