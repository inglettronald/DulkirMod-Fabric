package com.dulkirfabric.commands

import com.dulkirfabric.DulkirModFabric
import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.google.common.eventbus.Subscribe
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

object ConfigCommand {
    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
        dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("dulkir").executes {
            println("command running")
            DulkirModFabric.openScreenDelayed(DulkirConfig().screen)
            return@executes 0
        })
    }
}