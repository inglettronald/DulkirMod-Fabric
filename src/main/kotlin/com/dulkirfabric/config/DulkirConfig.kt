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
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.Modifier
import me.shedaniel.clothconfig2.api.ModifierKeyCode
import me.shedaniel.clothconfig2.impl.builders.LongListBuilder
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.InputUtil
import net.minecraft.text.LiteralTextContent
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

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
        val entryBuilder = builder.entryBuilder()
        val testing = builder.getOrCreateCategory(Text.translatable("category.cloth-config.testing"))
        testing.addEntry(
            entryBuilder.startKeyCodeField(Text.literal("Cool Key"), InputUtil.UNKNOWN_KEY)
                .setDefaultValue(InputUtil.UNKNOWN_KEY).build()
        )
        testing.addEntry(
            entryBuilder.startModifierKeyCodeField(
                Text.literal("Cool Modifier Key"),
                ModifierKeyCode.of(InputUtil.Type.KEYSYM.createFromCode(79), Modifier.of(false, true, false))
            ).setDefaultValue(
                ModifierKeyCode.of(
                    InputUtil.Type.KEYSYM.createFromCode(79),
                    Modifier.of(false, true, false)
                )
            ).build()
        )
        testing.addEntry(
            entryBuilder.startDoubleList(Text.literal("A list of Doubles"), mutableListOf(1.0, 2.0, 3.0))
                .setDefaultValue(
                    mutableListOf(1.0, 2.0, 3.0)
                ).build()
        )
        testing.addEntry(
            (entryBuilder.startLongList(Text.literal("A list of Longs"), mutableListOf(1L, 2L, 3L)).setDefaultValue(
                mutableListOf(1L, 2L, 3L)
            ).setInsertButtonEnabled(false) as LongListBuilder).build()
        )
        testing.addEntry(
            entryBuilder.startStrList(Text.literal("A list of Strings"), mutableListOf("abc", "xyz")).setTooltip(
                *arrayOf<Text>(
                    Text.literal("Yes this is some beautiful tooltip\nOh and this is the second line!")
                )
            ).setDefaultValue(mutableListOf("abc", "xyz")).build()
        )
        val colors = entryBuilder.startSubCategory(Text.literal("Colors")).setExpanded(true)
        colors.add(entryBuilder.startColorField(Text.literal("A color field"), 65535).setDefaultValue(65535).build())
        colors.add(
            entryBuilder.startColorField(Text.literal("An alpha color field"), -16711681).setDefaultValue(-16711681)
                .setAlphaMode(true).build()
        )
        colors.add(
            entryBuilder.startColorField(Text.literal("An alpha color field"), -1).setDefaultValue(-65536)
                .setAlphaMode(true).build()
        )
        builder.transparentBackground()
        screen = builder.build()
    }

}