package com.dulkirfabric

import com.dulkirfabric.DulkirModFabric.EVENT_BUS
import com.dulkirfabric.commands.ConfigCommand
import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

/**
 * Collection of different mod registration stuff ran on initializing the mod. It is separated for readability
 * purposes, as the list of features is planned to be quite large.
 */
object Registrations {

    fun registerCommands() {
        val cre = ClientCommandRegistrationCallback.EVENT
        cre.register(ConfigCommand::register)
    }

    fun registerEventListeners() {
        EVENT_BUS.subscribe(DulkirModFabric)
    }
}