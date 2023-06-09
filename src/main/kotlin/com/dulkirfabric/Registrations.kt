package com.dulkirfabric

import com.dulkirfabric.DulkirModFabric.EVENT_BUS
import com.dulkirfabric.commands.ConfigCommand
import com.dulkirfabric.commands.JoinDungeonCommands
import com.dulkirfabric.events.ClientTickEvent
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents


/**
 * Collection of different mod registration stuff ran on initializing the mod. It is separated for readability
 * purposes, as the list of features is planned to be quite large.
 */
object Registrations {

    fun registerCommands() {
        val cre = ClientCommandRegistrationCallback.EVENT
        cre.register(ConfigCommand::register)
        cre.register(JoinDungeonCommands.F1Command::register)
        cre.register(JoinDungeonCommands.F2Command::register)
        cre.register(JoinDungeonCommands.F3Command::register)
        cre.register(JoinDungeonCommands.F4Command::register)
        cre.register(JoinDungeonCommands.F5Command::register)
        cre.register(JoinDungeonCommands.F6Command::register)
        cre.register(JoinDungeonCommands.F7Command::register)
        cre.register(JoinDungeonCommands.M1Command::register)
        cre.register(JoinDungeonCommands.M2Command::register)
        cre.register(JoinDungeonCommands.M3Command::register)
        cre.register(JoinDungeonCommands.M4Command::register)
        cre.register(JoinDungeonCommands.M5Command::register)
        cre.register(JoinDungeonCommands.M6Command::register)
        cre.register(JoinDungeonCommands.M7Command::register)
    }

    fun registerEventListeners() {
        EVENT_BUS.subscribe(DulkirModFabric)
    }

    fun registerEvents() {
        // Register Custom Tick event, so we can use it like 1.8.9 forge
        ClientTickEvents.START_CLIENT_TICK.register(
            ClientTickEvents.StartTick { _ -> EVENT_BUS.post(ClientTickEvent.get()) }
        )
        // WorldLoadFinishedEvent TODO
    }
}