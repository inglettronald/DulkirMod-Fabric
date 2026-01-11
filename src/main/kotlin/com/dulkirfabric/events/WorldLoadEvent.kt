package com.dulkirfabric.events

import com.dulkirfabric.events.base.Event
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.Level

data class WorldLoadEvent(
    val server: MinecraftServer,
    val level: Level
): Event()
