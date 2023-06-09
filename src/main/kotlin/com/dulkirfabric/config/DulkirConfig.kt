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

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.ListHelper.mkKeyField
import com.dulkirfabric.config.ListHelper.mkStringField
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.InputUtil
import net.minecraft.client.util.InputUtil.UNKNOWN_KEY
import net.minecraft.text.LiteralTextContent
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import java.io.File

class DulkirConfig {

    private val buttonText: Text =
        MutableText.of(LiteralTextContent("Dulkir")).formatted(Formatting.BOLD, Formatting.YELLOW)
    var screen: Screen

    init {
        val builder = ConfigBuilder.create().setTitle(buttonText)
        builder.setDefaultBackgroundTexture(Identifier("minecraft:textures/block/oak_planks.png"))
        builder.setGlobalized(true)
        builder.setGlobalizedExpanded(true)
        builder.setParentScreen(mc.currentScreen)
        builder.setSavingRunnable(::saveConfig)
        val entryBuilder = builder.entryBuilder()
        val general = builder.getOrCreateCategory(Text.literal("General"))
        general.addEntry(
            entryBuilder.startBooleanToggle(Text.literal("Custom Inventory Scale Toggle"), configOptions.invScaleBool)
                .setTooltip(Text.literal("WAHOO!"))
                .setSaveConsumer { newValue -> configOptions.invScaleBool = newValue }
                .build()
        )
        general.addEntry(
            entryBuilder.startIntSlider(Text.literal("Inventory Scale"), configOptions.inventoryScale, 1, 5)
                .setTooltip(Text.literal("Size of GUI whenever you're in an inventory screen"))
                .setSaveConsumer { newValue -> configOptions.inventoryScale = newValue }
                .build()
        )

        val shortcuts = builder.getOrCreateCategory(Text.literal("Shortcuts"))
        shortcuts.addEntry(
            ListHelper.mkConfigList(
                Text.literal("Macros"),
                configOptions::macrosList,
                { Macro(UNKNOWN_KEY, "") },
                Text.literal("Macro"),
                { value ->
                    listOf(
                        entryBuilder.mkStringField(Text.literal("Command"), value::command),
                        entryBuilder.mkKeyField(Text.literal("KeyBinding"), value::keyBinding)
                    )
                }
            )
        )

        builder.transparentBackground()
        screen = builder.build()
    }

    data class ConfigOptions(
        var invScaleBool: Boolean = true,
        var inventoryScale: Int = 1,
        var macrosList: List<Macro> = listOf(Macro(UNKNOWN_KEY, ""))
    )

    @Serializable
    data class Macro(
        var keyBinding: InputUtil.Key,
        var command: String,
    )

    /**
     * Object for storing all the actual config values that will be used in doing useful stuff with the config
     */
    companion object ConfigVars {

        var configOptions = ConfigOptions()
        private fun saveConfig() {
            val json = Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                encodeDefaults = true
            }

            val configDirectory = File(mc.runDirectory, "config")
            if (!configDirectory.exists()) {
                configDirectory.mkdir()
            }
            val configFile = File(configDirectory, "dulkirConfig.json")
            configFile.writeText(json.encodeToString(configOptions))
        }

        fun loadConfig() {
            val configDir = File(mc.runDirectory, "config")
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

        }
    }

}