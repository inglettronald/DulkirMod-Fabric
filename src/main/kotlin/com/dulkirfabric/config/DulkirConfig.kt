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

package com.dulkirfabric.config

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.ListHelper.mkIntField
import com.dulkirfabric.config.ListHelper.mkStringField
import com.google.gson.Gson
import kotlinx.serialization.Serializable
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.SerializedName
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.LiteralTextContent
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import java.io.File
import java.util.*

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
            entryBuilder.startBooleanToggle(Text.literal("Custom Inventory Scale Toggle"), invScaleBool)
                .setTooltip(Text.literal("WAHOO!"))
                .setSaveConsumer { newValue -> invScaleBool = newValue }
                .build()
        )
        general.addEntry(
            entryBuilder.startFloatField(Text.literal("Inventory Scale"), inventoryScale)
                .setTooltip(Text.literal("Size of GUI whenever you're in an inventory screen"))
                .setSaveConsumer { newValue -> inventoryScale = newValue }
                .build()
        )
        val shortcuts = builder.getOrCreateCategory(Text.literal("Shortcuts"))
        shortcuts.addEntry(
            ListHelper.mkConfigList(
                Text.literal("Macros"),
                ListHelper.Holder::macros,
                { Macro(-2, "") },
                Text.literal("Macro"),
                { value ->
                    listOf(
                        entryBuilder.mkStringField(Text.literal("Command"), value::command),
                        entryBuilder.mkIntField(Text.literal("KeyBinding"), value::keyBinding)
                    )
                }
            )
        )

        builder.transparentBackground()
        screen = builder.build()
    }

    data class ConfigOptions(
        @SerializedName("testOption")
        val invScaleBool: Boolean,

        @SerializedName("inventoryScale")
        val inventoryScale: Float
    )

    @Serializable
    data class Macro(
        var keyBinding: Int,
        var command: String,
    )

    /**
     * Object for storing all the actual config values that will be used in doing useful stuff with the config
     */
    companion object ConfigVars {
        var invScaleBool: Boolean = true
        var inventoryScale: Float = 1f
        var value: List<Pair<Int, Int>> = listOf(Pair(1, 2), Pair(3, 4))


        private fun saveConfig() {
            val gson = Gson()
            val configOptions = ConfigOptions(
                invScaleBool,
                inventoryScale)
            val json = gson.toJson(configOptions)

            val configDirectory = File(mc.runDirectory, "config")
            if (!configDirectory.exists()) {
                configDirectory.mkdir()
            }
            val configFile = File(configDirectory, "dulkirConfig.json")
            configFile.writeText(json)
        }

        fun loadConfig() {
            val gson = Gson()
            val configDir = File(mc.runDirectory, "config")
            if (!configDir.exists()) return
            val configFile = File(configDir, "dulkirConfig.json")
            if (configFile.exists()) {
                val json = configFile.readText()
                val configOptions = gson.fromJson(json, ConfigOptions::class.java)

                invScaleBool = configOptions.invScaleBool
                inventoryScale = configOptions.inventoryScale
            }
        }
    }

}