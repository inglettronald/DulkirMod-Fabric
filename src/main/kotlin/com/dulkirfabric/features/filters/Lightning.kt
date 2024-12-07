package com.dulkirfabric.features.filters

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.EntityLoadEvent
import com.dulkirfabric.util.Utils
import meteordevelopment.orbit.EventHandler
import net.minecraft.entity.Entity
import net.minecraft.entity.LightningEntity
import net.minecraft.world.event.GameEvent

object Lightning {
    @EventHandler
    fun cull(event: EntityLoadEvent) {
        if (!DulkirConfig.configOptions.hideLightning) return
        if (!Utils.isInSkyblock()) return
        if (event.entity is LightningEntity) {
            event.entity.remove(Entity.RemovalReason.KILLED)
            event.entity.emitGameEvent(GameEvent.ENTITY_DIE)
        }
    }
}