package com.dulkirfabric.events

import com.dulkirfabric.events.base.Event
import net.minecraft.server.MinecraftServer
import net.minecraft.world.World

data class WorldLoadEvent(
    val server: MinecraftServer,
    val world: World
): Event()
