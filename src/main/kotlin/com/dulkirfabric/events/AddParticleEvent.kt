package com.dulkirfabric.events

import com.dulkirfabric.events.base.CancellableEvent
import net.minecraft.client.particle.Particle

data class AddParticleEvent(
    val particle: Particle
) : CancellableEvent()