package com.dulkirfabric

import com.dulkirfabric.DulkirModFabric.EVENT_BUS
import com.dulkirfabric.commands.*
import com.dulkirfabric.events.*
import com.dulkirfabric.events.chat.ChatReceivedEvent
import com.dulkirfabric.events.chat.ModifyCommandEvent
import com.dulkirfabric.events.chat.OverlayReceivedEvent
import com.dulkirfabric.features.*
import com.dulkirfabric.features.chat.AbiPhoneDND
import com.dulkirfabric.features.chat.BridgeBotFormatter
import com.dulkirfabric.util.TablistUtils
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.fabricmc.loader.api.FabricLoader


/**
 * Collection of different mod registration stuff ran on initializing the mod. It is separated for readability
 * purposes, as the list of features is planned to be quite large.
 */
object Registrations {
    private var tickCount: Int = 0

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
        cre.register(AnimationCommand::register)
        if (FabricLoader.getInstance().isDevelopmentEnvironment)
            cre.register(TestCommand::register)
    }

    fun registerEventListeners() {
        EVENT_BUS.subscribe(DulkirModFabric)
        EVENT_BUS.subscribe(KeyShortCutImpl)
        EVENT_BUS.subscribe(RenderTest)
        EVENT_BUS.subscribe(TooltipImpl)
        EVENT_BUS.subscribe(CustomBlockOutline)
        EVENT_BUS.subscribe(AbiPhoneDND)
        EVENT_BUS.subscribe(InventoryScale)
        EVENT_BUS.subscribe(IPhoneAlarm)
        EVENT_BUS.subscribe(AliasImpl)
        EVENT_BUS.subscribe(EffigyDisplay)
        EVENT_BUS.subscribe(TablistUtils)
        EVENT_BUS.subscribe(CullExplosionParticles)
        EVENT_BUS.subscribe(CooldownDisplays)
        EVENT_BUS.subscribe(ArachneFeatures)
        EVENT_BUS.subscribe(BridgeBotFormatter)
    }

    fun registerEvents() {
        // Register Custom Tick event, so we can use it like 1.8.9 forge
        ClientTickEvents.START_CLIENT_TICK.register { _ ->
            ClientTickEvent.post()
            if (tickCount % 20 == 0) LongUpdateEvent.post()
            tickCount++
        }
        ClientReceiveMessageEvents.ALLOW_GAME.register { message, overlay ->
            if (overlay) !OverlayReceivedEvent(message.toString()).post()
            else !ChatReceivedEvent(message).post()
        }

        ClientSendMessageEvents.MODIFY_COMMAND.register { command ->
            ModifyCommandEvent(command).also { it.post() }.command
        }

        WorldRenderEvents.END.register { context -> WorldRenderLastEvent(context).post() }

        ScreenEvents.BEFORE_INIT.register(
            ScreenEvents.BeforeInit { client, screen, scaledWidth, scaledHeight ->
                ScreenMouseEvents.beforeMouseScroll(screen)
                    .register(ScreenMouseEvents.BeforeMouseScroll { coolScreen, mouseX, mouseY, horizontalAmount, verticalAmount ->
                        MouseScrollEvent(coolScreen, mouseX, mouseY, horizontalAmount, verticalAmount).post()
                    })
            }
        )

        WorldRenderEvents.BLOCK_OUTLINE.register { worldRenderContext, blockOutlineContext ->
            !BlockOutlineEvent(worldRenderContext, blockOutlineContext).post()
        }
        ClientEntityEvents.ENTITY_LOAD.register { entity, world ->
            EntityLoadEvent(entity, world).post()
        }
        ServerWorldEvents.LOAD.register { server, world ->
            WorldLoadEvent(server, world).post()
        }
    }
}