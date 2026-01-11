package com.dulkirfabric.config

import com.mojang.blaze3d.platform.InputConstants
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.gui.entries.MultiElementListEntry
import me.shedaniel.clothconfig2.gui.entries.NestedListListEntry
import net.minecraft.network.chat.Component
import java.util.*
import kotlin.reflect.KMutableProperty0

object ConfigHelper {
    fun <T> mkConfigList(
        name: Component,
        property: KMutableProperty0<List<T>>,
        newT: () -> T,
        elementName: Component,
        render: (T) -> List<AbstractConfigListEntry<*>>,
        canDelete: Boolean = true,
    ): NestedListListEntry<T, MultiElementListEntry<T>> {
        return NestedListListEntry<T, MultiElementListEntry<T>>(
            name, // field name
            property.get(), // value
            false, // defaultExpanded
            { Optional.empty() }, // tooltipSupplier
            { property.set(it) }, // saveConsumer
            { mutableListOf() }, // defaultValue
            Component.literal("Reset"), // resetButtonKey
            canDelete,
            false,
            { value, entry -> // createNewCell
                val value = value ?: newT()
                MultiElementListEntry(elementName, value, render(value), true)
            }
        )
    }

    fun ConfigEntryBuilder.mkStringField(text: Component, prop: KMutableProperty0<String>) = startStrField(text, prop.get())
        .setSaveConsumer { prop.set(it) }
        .setDefaultValue("")
        .build()
    fun ConfigEntryBuilder.mkIntField(text: Component, prop: KMutableProperty0<Int>) = startIntField(text, prop.get())
        .setSaveConsumer { prop.set(it) }
        .setDefaultValue(0)
        .build()

    fun ConfigEntryBuilder.mkKeyField(text: Component, prop: KMutableProperty0<InputConstants.Key>) = startKeyCodeField(text, prop.get())
        .setKeySaveConsumer { prop.set(it) }
        .setDefaultValue(InputConstants.UNKNOWN)
        .build()

    fun ConfigEntryBuilder.mkToggle(text: Component, prop: KMutableProperty0<Boolean>, tooltip: Component = Component.literal(""))
    = startBooleanToggle(text, prop.get())
        .setSaveConsumer { prop.set(it) }
        .setDefaultValue(false)
        .setTooltip(tooltip)
        .build()
}