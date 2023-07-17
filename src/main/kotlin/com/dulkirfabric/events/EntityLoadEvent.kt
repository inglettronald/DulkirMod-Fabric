package com.dulkirfabric.events

import com.dulkirfabric.events.base.Event
import net.minecraft.entity.Entity
import net.minecraft.world.World

data class EntityLoadEvent(
    val entity: Entity,
    val world: World
): Event()
