package com.dulkirfabric.features.filters

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.AddParticleEvent
import com.dulkirfabric.events.EntityLoadEvent
import com.dulkirfabric.util.Utils
import meteordevelopment.orbit.EventHandler
import net.minecraft.entity.LightningEntity

object Lightning {
    @EventHandler
    fun cull(event: EntityLoadEvent) {
        if (!DulkirConfig.configOptions.hideLightning) return
        if (!Utils.isInSkyblock()) return
        if (event.entity is LightningEntity) {
           event.entity.kill()
        }
    }
}