package com.dulkirfabric.features.filters

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.AddParticleEvent
import meteordevelopment.orbit.EventHandler
import net.minecraft.client.particle.ExplosionLargeParticle

/**
 * Remove nearby Explosions that would be intrusive visually
 */
object CullExplosionParticles {

    @EventHandler
    fun onParticle(event: AddParticleEvent) {
        if (!DulkirConfig.configOptions.disableExplosionParticles) return
        val particle = event.particle
        if (particle is ExplosionLargeParticle) {
            event.cancel()
        }
    }
}