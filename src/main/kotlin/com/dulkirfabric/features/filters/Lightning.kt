package com.dulkirfabric.features.filters

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.EntityLoadEvent
import com.dulkirfabric.util.Utils
import meteordevelopment.orbit.EventHandler
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LightningBolt
import net.minecraft.world.level.gameevent.GameEvent

object Lightning {
    @EventHandler
    fun cull(event: EntityLoadEvent) {
        if (!DulkirConfig.configOptions.hideLightning) return
        if (!Utils.isInSkyblock()) return
        if (event.entity is LightningBolt) {
            event.entity.remove(Entity.RemovalReason.KILLED)
            event.entity.gameEvent(GameEvent.ENTITY_DIE)
        }
    }
}