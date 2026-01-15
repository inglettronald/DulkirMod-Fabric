package com.dulkirfabric.events

import com.dulkirfabric.events.base.Event
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level

data class EntityLoadEvent(
    val entity: Entity,
    val level: Level
): Event()
