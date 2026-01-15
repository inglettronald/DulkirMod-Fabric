package com.dulkirfabric.util

import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource

object SoundUtil {
    val iphone = SoundEvent.createVariableRangeEvent(ResourceLocation.parse("dulkirmod:iphone"))

    fun stop(sounds: List<SoundInstance>) {
        val m = Minecraft.getInstance().soundManager
        sounds.forEach(m::stop)
    }

    fun playSoundAtPlayer(event: SoundEvent): SimpleSoundInstance {
        val instance = SimpleSoundInstance(
            event,
            SoundSource.MASTER,
            1F,
            1F,
            SingleThreadedRandomSource(0L),
            Minecraft.getInstance().player?.blockPosition() ?: BlockPos(0, 0, 0)
        )
        Minecraft.getInstance().soundManager.play(instance)
        return instance
    }

    fun playIPhoneAlarm(): SimpleSoundInstance {
        return playSoundAtPlayer(iphone)
    }
}