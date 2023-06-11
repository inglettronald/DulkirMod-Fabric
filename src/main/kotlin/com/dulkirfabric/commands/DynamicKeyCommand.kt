package com.dulkirfabric.commands

import com.dulkirfabric.util.TextUtils
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

object DynamicKeyCommand {
    var command = ""
    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<FabricClientCommandSource>("dk")
                .executes { context ->
                    TextUtils.info("§6Usage: /dk set <command>")
                    TextUtils.info("§6For more information about this command, run /dk help")
                    return@executes 0
                }
                .then(
                    LiteralArgumentBuilder.literal<FabricClientCommandSource>("set")
                        .then(
                            RequiredArgumentBuilder.argument<FabricClientCommandSource, String>("command", RestArgumentType)
                                .executes { context ->
                                    command = StringArgumentType.getString(context, "command")
                                    command = command.removePrefix("/")
                                    TextUtils.info("§6§lCommand Registered: §7$command")
                                    return@executes 1
                                }
                        )
                )
                .then(
                    LiteralArgumentBuilder.literal<FabricClientCommandSource>("help")
                        .executes {
                            TextUtils.info("§6§lDynamic Keybind Info")
                            TextUtils.info("§7 - There's a keybind setting inside your Dulkir Config you can use in order" +
                                    " to make a chat macro for a particular in game command. This only works for commands.", prefix = false)
                            TextUtils.info("§7 - Usage: /dk set <command args>", prefix = false)
                            TextUtils.info("§7 (i made this cuz I have a mouse button that i use for a bunch of different useful " +
                                    "actions depending upon what I'm doing, so don't worry if this feature doesn't apply to you lol)", prefix = false)
                            return@executes 2
                        }
                )
        )
    }

}