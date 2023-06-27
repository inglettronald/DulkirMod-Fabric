package com.dulkirfabric.commands

import com.dulkirfabric.util.ScoreBoardUtils
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

object TestCommand {
    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
        dispatcher.register(LiteralArgumentBuilder.literal<FabricClientCommandSource>("linetest").executes {
            val lines = ScoreBoardUtils.getLines()
            return@executes 0
        })
    }
}