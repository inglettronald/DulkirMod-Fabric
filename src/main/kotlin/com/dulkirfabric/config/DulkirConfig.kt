/**
 *   This Source Code Form is subject to the terms of the Mozilla Public
 *   License, v. 2.0. If a copy of the MPL was not distributed with this
 *   file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * If it is not possible or desirable to put the notice in a particular
 * file, then You may include the notice in a location (such as a LICENSE
 * file in a relevant directory) where a recipient would be likely to look
 * for such a notice.
 *
 * You may add additional accurate notices of copyright ownership.
 */
@file:UseSerializers(com.dulkirfabric.config.serializations.KeySerializer::class)
package com.dulkirfabric.config

import com.dulkirfabric.DulkirModFabric
import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.ConfigHelper.mkKeyField
import com.dulkirfabric.config.ConfigHelper.mkStringField
import com.dulkirfabric.config.ConfigHelper.mkToggle
import com.dulkirfabric.util.render.AnimationPreset
import com.dulkirfabric.util.render.HudElement
import com.mojang.blaze3d.platform.InputConstants
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.Json
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor
import net.minecraft.resources.ResourceLocation
import org.joml.Vector2i
import java.io.File

class DulkirConfig {

    private val buttonText: Component =
        Component.literal("Dulkir").withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW)
    var screen: Screen = buildScreen(null)

    fun buildScreen(parentScreen: Screen? = null): Screen {
        val builder = ConfigBuilder.create().setTitle(buttonText)
        builder.setDefaultBackgroundTexture(ResourceLocation.parse("minecraft:textures/block/oak_planks.png"))
        builder.setGlobalized(true)
        builder.setGlobalizedExpanded(false)
        builder.setParentScreen(mc.screen)
        builder.setSavingRunnable(::saveConfig)
        val entryBuilder = builder.entryBuilder()
        val general = builder.getOrCreateCategory(Component.literal("General"))
        general.addEntry(
            entryBuilder.mkToggle(
                Component.literal("Show Pause Menu Button"),
                configOptions::showPauseMenuButton,
                Component.literal("Provides a fast way to get to the config gui")
            )
        )
        general.addEntry(
            entryBuilder.mkToggle(
                Component.literal("Inventory Scale Toggle"),
                configOptions::invScaleBool,
                Component.literal("This is a tooltip")
            )
        )
        general.addEntry(
            entryBuilder.startFloatField(Component.literal("Inventory Scale"), configOptions.inventoryScale)
                .setTooltip(Component.literal("Size of GUI whenever you're in an inventory screen"))
                .setSaveConsumer { newValue ->
                    configOptions.inventoryScale = newValue
                }
                .build()
        )
        general.addEntry(
            entryBuilder.mkToggle(
                Component.literal("Scrollable/Scalable Tooltips"),
                configOptions::toolTipFeatures,
                Component.literal("Feature to help viewing large/long item lore.")
            )
        )
        general.addEntry(
            entryBuilder.startFloatField(Component.literal("Tooltip Scale"), configOptions.tooltipScale)
                .setTooltip(Component.literal("Default Value for Scaling a particular tooltip without scroll input"))
                .setSaveConsumer { newValue -> configOptions.tooltipScale = newValue }
                .build()
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Ignore Reverse Third Person"), configOptions::ignoreReverseThirdPerson)
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Disable Status Effect Rendering"), configOptions::statusEffectHidden)
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Custom Block outlines"), configOptions::customBlockOutlines)
        )
        general.addEntry(
            entryBuilder.startIntSlider(Component.literal("Line Thickness"), configOptions.blockOutlineThickness, 1, 5)
                .setSaveConsumer { newValue -> configOptions.blockOutlineThickness = newValue }
                .build()
        )
        general.addEntry(
            entryBuilder.startColorField(
                Component.literal("Outline Color"),
                TextColor.fromRgb(configOptions.blockOutlineColor)
            )
                .setSaveConsumer { newValue -> configOptions.blockOutlineColor = newValue }
                .build()
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Abiphone DND"), configOptions::abiPhoneDND)
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Abiphone Caller ID"), configOptions::abiPhoneCallerID)
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Inactive Effigy Waypoints"), configOptions::inactiveEffigyDisplay)
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Disable Explosion Particles"), configOptions::disableExplosionParticles)
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Durability-Based Cooldown Display"), configOptions::duraCooldown)
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Hide Armor Overlay in Skyblock"), configOptions::hideArmorOverlay)
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Hide Hunger Overlay in Skyblock"), configOptions::hideHungerOverlay)
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Hide Fire Overlay"), configOptions::hideFireOverlay)
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Hide Lightning (SkyBlock only)"), configOptions::hideLightning)
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Hide Non-Crits"), configOptions::hideNonCrits)
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Hide Crits"), configOptions::hideCrits)
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Truncate Crits"), configOptions::truncateDamage)
        )
        general.addEntry(
            entryBuilder.startIntSlider(Component.literal("Anti Downtime Alarm"), configOptions.alarmTimeout, 0, 1000)
                .setSaveConsumer {
                    configOptions.alarmTimeout = it
                }
                .setTooltip(Component.literal("Set to 0 to disable. (Time in seconds)"))
                .build()
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Arachne Keeper Waypoints"), configOptions::arachneKeeperWaypoints)
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Arachne Boss Spawn Timer"), configOptions::arachneSpawnTimer)
        )
        general.addEntry(
            entryBuilder.mkToggle(
                Component.literal("Convert Action Bar to HUD elements"), configOptions::hudifyActionBar,
                tooltip = Component.literal("This converts Mana/Health/Def/Stacks as HUD elements")
            )
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Show Speed in HUD"), configOptions::speedHud)
        )
        general.addEntry(
            entryBuilder.mkToggle(
                Component.literal("Include EHP in def HUD element"), configOptions::showEHP,
                tooltip = Component.literal("Must have Action Bar HUD elements Enabled")
            )
        )
        general.addEntry(
            entryBuilder.mkToggle(
                Component.literal("Hide Held Item Tooltips"), configOptions::hideHeldItemTooltip,
                tooltip = Component.literal("This is for the pesky overlay that pops up on switching items")
            )
        )
        general.addEntry(
            entryBuilder.mkToggle(
                Component.literal("Etherwarp Preview"), configOptions::showEtherwarpPreview,
                tooltip = Component.literal("Highlights the targeted block when shifting with a aotv.")
            )
        )
        general.addEntry(
            entryBuilder.startAlphaColorField(
                Component.literal("Etherwarp Valid Preview Color"),
                configOptions.etherwarpPreviewColor
            )
                .setDefaultValue(0x8800FF00.toInt())
                .setSaveConsumer { newValue -> configOptions.etherwarpPreviewColor = newValue }
                .build()
        )
        general.addEntry(
            entryBuilder.startAlphaColorField(
                Component.literal("Etherwarp Invalid Preview Color"),
                configOptions.etherwarpInvalidPreviewColor
            )
                .setDefaultValue(0x88FF0000.toInt())
                .setSaveConsumer { newValue -> configOptions.etherwarpInvalidPreviewColor = newValue }
                .build()
        )
        general.addEntry(
            entryBuilder.mkToggle(Component.literal("Broken Hype Notification"), configOptions::brokenHypNotif)
        )
        general.addEntry(
                entryBuilder.mkToggle(Component.literal("Hide Scoreboard numbers"), configOptions::hideScoreboardNumbers)
        )

        val shortcuts = builder.getOrCreateCategory(Component.literal("Shortcuts"))
        shortcuts.addEntry(
            entryBuilder.mkKeyField(Component.literal("Dynamic Key"), configOptions::dynamicKey)
        )
        shortcuts.addEntry(
            entryBuilder.mkToggle(
                Component.literal("Only Register Shortcuts in Skyblock"), configOptions::macrosSkyBlockOnly,
                Component.literal("Useful if you want to use some of these binds elsewhere for non-skyblock specific stuff.")
            )
        )
        shortcuts.addEntry(
            ConfigHelper.mkConfigList(
                Component.literal("Macros"),
                configOptions::macrosList,
                { Macro(InputConstants.UNKNOWN, "") },
                Component.literal("Macro"),
                { value ->
                    listOf(
                        entryBuilder.mkStringField(Component.literal("Command"), value::command),
                        entryBuilder.mkKeyField(Component.literal("KeyBinding"), value::keyBinding)
                    )
                }
            )
        )

        val aliases = builder.getOrCreateCategory(Component.literal("Shortcuts"))
        aliases.addEntry(
            ConfigHelper.mkConfigList(
                Component.literal("Aliases (do not include '/')"),
                configOptions::aliasList,
                { Alias("", "") },
                Component.literal("Alias"),
                { value ->
                    listOf(
                        entryBuilder.mkStringField(Component.literal("Command"), value::command),
                        entryBuilder.mkStringField(Component.literal("Alias"), value::alias)
                    )
                }
            )
        )
        val animations = builder.getOrCreateCategory(Component.literal("Animations"))

        //TODO: Come up with some custome float slider instead of int slider jank
        animations.addEntry(
            entryBuilder.startIntSlider(Component.literal("posX"), configOptions.animationPreset.posX, -150, 150)
                .setSaveConsumer { newValue -> configOptions.animationPreset.posX = newValue }
                .setDefaultValue(0)
                .build()
        )
        animations.addEntry(
            entryBuilder.startIntSlider(Component.literal("posY"), configOptions.animationPreset.posY, -150, 150)
                .setSaveConsumer { newValue -> configOptions.animationPreset.posY = newValue }
                .setDefaultValue(0)
                .build()
        )
        animations.addEntry(
            entryBuilder.startIntSlider(Component.literal("posZ"), configOptions.animationPreset.posZ, -150, 50)
                .setSaveConsumer { newValue -> configOptions.animationPreset.posZ = newValue }
                .setDefaultValue(0)
                .build()
        )
        animations.addEntry(
            entryBuilder.startIntSlider(Component.literal("rotationX"), configOptions.animationPreset.rotX, -180, 180)
                .setSaveConsumer { newValue -> configOptions.animationPreset.rotX = newValue }
                .setDefaultValue(0)
                .build()
        )
        animations.addEntry(
            entryBuilder.startIntSlider(Component.literal("rotationY"), configOptions.animationPreset.rotY, -180, 180)
                .setSaveConsumer { newValue -> configOptions.animationPreset.rotY = newValue }
                .setDefaultValue(0)
                .build()
        )
        animations.addEntry(
            entryBuilder.startIntSlider(Component.literal("rotationZ"), configOptions.animationPreset.rotZ, -180, 180)
                .setSaveConsumer { newValue -> configOptions.animationPreset.rotZ = newValue }
                .setDefaultValue(0)
                .build()
        )
        animations.addEntry(
            entryBuilder.startFloatField(Component.literal("Held Item Scale"), configOptions.animationPreset.scale)
                .setTooltip(Component.literal("Recommended range of .1 - 2"))
                .setSaveConsumer { newValue ->
                    configOptions.animationPreset.scale = newValue
                }
                .setDefaultValue(1f)
                .build()
        )
        animations.addEntry(
            entryBuilder.startIntSlider(Component.literal("Swing Duration"), configOptions.animationPreset.swingDuration, 2, 20)
                .setSaveConsumer { newValue -> configOptions.animationPreset.swingDuration = newValue }
                .setDefaultValue(6)
                .build()
        )
        animations.addEntry(
            entryBuilder.startBooleanToggle(
                Component.literal("Cancel Re-Equip Animation"),
                configOptions.animationPreset.cancelReEquip
            )
                .setSaveConsumer { newValue -> configOptions.animationPreset.cancelReEquip = newValue }
                .setDefaultValue(false)
                .build()
        )

        val bridge = builder.getOrCreateCategory(Component.literal("Bridge Features"))

        bridge.addEntry(
            entryBuilder.mkToggle(Component.literal("Format Bridge Messages"), configOptions::bridgeFormatter)
        )
        bridge.addEntry(
            entryBuilder.mkStringField(Component.literal("Bridge Bot IGN"), configOptions::bridgeBotName)
        )
        bridge.addEntry(
            entryBuilder.startColorField(Component.literal("Bridge User Color"), configOptions.bridgeNameColor)
                .setDefaultValue(ChatFormatting.GOLD.color!!)
                .setSaveConsumer { newValue -> configOptions.bridgeNameColor = newValue }
                .build()
        )

        val slayer = builder.getOrCreateCategory(Component.literal("Slayer"))
        slayer.addEntry(
            entryBuilder.mkToggle(Component.literal("MiniBoss Highlight Box"), configOptions::boxMinis)
        )
        slayer.addEntry(
            entryBuilder.mkToggle(Component.literal("MiniBoss Announcement Alert"), configOptions::announceMinis)
        )
        slayer.addEntry(
            entryBuilder.mkToggle(
                Component.literal("Show Kill Time on Slayer Completion"), configOptions::slayerKillTime,
                Component.literal("Shows up in chat!")
            )
        )
        slayer.addEntry(
            entryBuilder.mkToggle(
                Component.literal("Blaze Slayer Attunement Display"), configOptions::attunementDisplay,
                Component.literal("Shows a wireframe in the correct color for the slayer.")
            )
        )
        slayer.addEntry(
            entryBuilder.mkToggle(
                Component.literal("Disable ALL particles during Blaze slayer boss"),
                configOptions::cleanBlaze
            )
        )
        slayer.addEntry(
            entryBuilder.mkToggle(
                Component.literal("Vampire Steak Display"), configOptions::steakDisplay,
                Component.literal("Shows a wireframe on vampire boss when you can 1 tap it")
            )
        )
        slayer.addEntry(
            entryBuilder.mkToggle(
                Component.literal("Blood Ichor Highlight"), configOptions::ichorHighlight,
                Component.literal("Highlights the T5 mechanic that you line up with the boss.")
            )
        )

        val garden = builder.getOrCreateCategory(Component.literal("Garden"))
        garden.addEntry(
            entryBuilder.mkToggle(Component.literal("Show Visitor Info in HUD"), configOptions::visitorHud)
        )
        garden.addEntry(
            entryBuilder.mkToggle(Component.literal("Show Composter Info in HUD"), configOptions::showComposterInfo)
        )
        garden.addEntry(
            entryBuilder.mkToggle(Component.literal("Show Title alert when max visitors"), configOptions::visitorAlert)
        )
        garden.addEntry(
            entryBuilder.mkToggle(
                Component.literal("Persistent Visitor alert (dependent on previous)"),
                configOptions::persistentVisitorAlert
            )
        )
        garden.addEntry(
            entryBuilder.mkToggle(Component.literal("Show Blocks per second (SPEED)"), configOptions::speedBpsHud)
        )
        garden.addEntry(
            entryBuilder.mkToggle(Component.literal("Show Pitch/Yaw in HUD"), configOptions::pitchYawDisplay)
        )
        builder.transparentBackground()
        if (parentScreen != null)
            builder.setParentScreen(parentScreen)
        return builder.build()
    }

    @Serializable
    data class ConfigOptions(
        var invScaleBool: Boolean = false,
        var inventoryScale: Float = 1f,
        var macrosList: List<Macro> = listOf(Macro(InputConstants.UNKNOWN, "")),
        var macrosSkyBlockOnly: Boolean = false,
        var aliasList: List<Alias> = listOf(Alias("", "")),
        var ignoreReverseThirdPerson: Boolean = false,
        var dynamicKey: InputConstants.Key = InputConstants.UNKNOWN,
        var customBlockOutlines: Boolean = false,
        var blockOutlineThickness: Int = 3,
        var blockOutlineColor: Int = 0xFFFFFF,
        var abiPhoneDND: Boolean = false,
        var abiPhoneCallerID: Boolean = false,
        var toolTipFeatures: Boolean = false,
        var tooltipScale: Float = 1f,
        var statusEffectHidden: Boolean = false,
        var inactiveEffigyDisplay: Boolean = false,
        var disableExplosionParticles: Boolean = false,
        var hideArmorOverlay: Boolean = false,
        var hideHungerOverlay: Boolean = false,
        var animationPreset: AnimationPreset = AnimationPreset(),
        var duraCooldown: Boolean = false,
        var alarmTimeout: Int = 0,
        var arachneKeeperWaypoints: Boolean = false,
        var arachneSpawnTimer: Boolean = false,
        var bridgeFormatter: Boolean = false,
        var bridgeBotName: String = "Dilkur",
        var bridgeNameColor: Int = ChatFormatting.GOLD.color!!,
        val positions: MutableMap<String, HudElement.HudMeta> = mutableMapOf(),
        var hudifyActionBar: Boolean = false,
        var showEHP: Boolean = false,
        var hideHeldItemTooltip: Boolean = false,
        var showEtherwarpPreview: Boolean = false,
        var etherwarpPreviewColor: Int = 0x8800FF00.toInt(),
        var etherwarpInvalidPreviewColor: Int = 0x88FF0000.toInt(),
        var announceMinis: Boolean = false,
        var boxMinis: Boolean = false,
        var attunementDisplay: Boolean = false,
        var hideFireOverlay: Boolean = false,
        var hideLightning: Boolean = false,
        var cleanBlaze: Boolean= false,
        var timeSlayerBoss: Boolean = false,
        var hideNonCrits: Boolean = false,
        var truncateDamage: Boolean = false,
        var hideCrits: Boolean = false,
        var visitorHud: Boolean = false,
        var showComposterInfo: Boolean = false,
        var slayerKillTime: Boolean = false,
        var visitorAlert: Boolean = false,
        var persistentVisitorAlert: Boolean = false,
        var brokenHypNotif: Boolean = false,
        var steakDisplay: Boolean = false,
        var ichorHighlight: Boolean = false,
        var speedHud: Boolean = false,
        var speedBpsHud: Boolean = false,
        var pitchYawDisplay: Boolean = false,
        var hideScoreboardNumbers: Boolean = false,
        var showPauseMenuButton: Boolean = false
    )

    @Serializable
    data class Macro(
        var keyBinding: InputConstants.Key,
        var command: String,
    )

    @Serializable
    data class Alias(
        var alias: String,
        var command: String
    )

    /**
     * Object for storing all the actual config values that will be used in doing useful stuff with the config
     */
    companion object ConfigVars {

        var configOptions = ConfigOptions()

        val huds = mutableListOf<Triple<HudElement, Vector2i, Float>>()

        fun registerHud(
            id: String, label: Component, width: Int, height: Int,
            defaultPosition: Vector2i, scale: Float = 1f
        ): HudElement {
            val element = HudElement(
                configOptions.positions.getOrPut(
                    id
                ) { HudElement.HudMeta(defaultPosition.x, defaultPosition.y, scale) },
                ResourceLocation.fromNamespaceAndPath(DulkirModFabric.MOD_ID, id),
                label, width, height,
            )
            huds.add(Triple(element, defaultPosition, scale))
            return element
        }

        fun saveConfig() {
            val json = Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                encodeDefaults = true
            }

            val configDirectory = File(mc.gameDirectory, "config")
            if (!configDirectory.exists()) {
                configDirectory.mkdir()
            }
            val configFile = File(configDirectory, "dulkirConfig.json")
            configFile.writeText(json.encodeToString(configOptions))
        }

        fun loadConfig() {
            val configDir = File(mc.gameDirectory, "config")
            if (!configDir.exists()) return
            val configFile = File(configDir, "dulkirConfig.json")
            if (configFile.exists()) {
                val json = Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                }
                configOptions = json.decodeFromString<ConfigOptions>(configFile.readText())
            }
            huds.forEach { (element, defaultPosition, scale) ->
                element.meta = configOptions.positions.getOrPut(
                    element.identifier.path
                ) { HudElement.HudMeta(defaultPosition.x, defaultPosition.y, scale) }
            }
        }
    }
}