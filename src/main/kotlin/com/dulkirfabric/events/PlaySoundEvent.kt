package com.dulkirfabric.events

import com.dulkirfabric.events.base.CancellableEvent
import net.minecraft.client.resources.sounds.SoundInstance

data class PlaySoundEvent(
    val sound: SoundInstance
): CancellableEvent()
