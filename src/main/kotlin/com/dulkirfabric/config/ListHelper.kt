package com.dulkirfabric.config

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.gui.entries.MultiElementListEntry
import me.shedaniel.clothconfig2.gui.entries.NestedListListEntry
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import java.util.*
import kotlin.reflect.KMutableProperty0

object ListHelper {
    fun <T> mkConfigList(
        name: Text,
        property: KMutableProperty0<List<T>>,
        newT: () -> T,
        elementName: Text,
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
            Text.literal("Reset"), // resetButtonKey
            canDelete,
            false,
            { value, entry -> // createNewCell
                val value = value ?: newT()
                MultiElementListEntry(elementName, value, render(value), true)
            }
        )
    }

    object Holder {
        var macros = listOf(DulkirConfig.Macro(InputUtil.UNKNOWN_KEY, "Hello World"))
    }

    fun ConfigEntryBuilder.mkStringField(text: Text, prop: KMutableProperty0<String>) = startStrField(text, prop.get())
        .setSaveConsumer { prop.set(it) }
        .setDefaultValue("")
        .build()
    fun ConfigEntryBuilder.mkIntField(text: Text, prop: KMutableProperty0<Int>) = startIntField(text, prop.get())
        .setSaveConsumer { prop.set(it) }
        .setDefaultValue(0)
        .build()

    fun ConfigEntryBuilder.mkKeyField(text: Text, prop: KMutableProperty0<InputUtil.Key>) = startKeyCodeField(text, prop.get())
        .setKeySaveConsumer { prop.set(it) }
        .setDefaultValue(InputUtil.UNKNOWN_KEY)
        .build()
}