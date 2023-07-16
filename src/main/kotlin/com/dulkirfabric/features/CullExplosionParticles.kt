package com.dulkirfabric.features

import com.dulkirfabric.events.AddParticleEvent
import meteordevelopment.orbit.EventHandler
import net.minecraft.client.particle.ExplosionLargeParticle

/**
 * Remove nearby Explosions that would be intrusive visually
 */
object CullExplosionParticles {

    @EventHandler
    fun onParticle(event: AddParticleEvent) {
        val particle = event.particle
        if (particle is ExplosionLargeParticle) {
            event.cancel()
        }
    }
}