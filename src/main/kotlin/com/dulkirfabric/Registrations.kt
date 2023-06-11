package com.dulkirfabric

import com.dulkirfabric.DulkirModFabric.EVENT_BUS
import com.dulkirfabric.commands.ConfigCommand
import com.dulkirfabric.commands.DynamicKeyCommand
import com.dulkirfabric.commands.JoinDungeonCommands
import com.dulkirfabric.events.ChatReceivedEvent
import com.dulkirfabric.events.ClientTickEvent
import com.dulkirfabric.events.OverlayReceivedEvent
import com.dulkirfabric.features.KeyShortCutImpl
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents


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
        cre.register(DynamicKeyCommand::register)
    }

    fun registerEventListeners() {
        EVENT_BUS.subscribe(DulkirModFabric)
        EVENT_BUS.subscribe(KeyShortCutImpl)
    }

    fun registerEvents() {
        // Register Custom Tick event, so we can use it like 1.8.9 forge
        ClientTickEvents.START_CLIENT_TICK.register(
            ClientTickEvents.StartTick { _ -> ClientTickEvent.post() }
        )
        ClientReceiveMessageEvents.ALLOW_GAME.register(
            ClientReceiveMessageEvents.AllowGame { message, overlay ->
                if (overlay) !OverlayReceivedEvent(message.toString()).post()
                else !ChatReceivedEvent(message.toString()).post()
            }
        )
    }
}