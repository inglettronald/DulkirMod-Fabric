package com.dulkirfabric.config

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.gui.entries.MultiElementListEntry
import me.shedaniel.clothconfig2.gui.entries.NestedListListEntry
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
        var macros = listOf(DulkirConfig.Macro(-1, "Hello World"))
    }

    fun ConfigEntryBuilder.mkStringField(text: Text, prop: KMutableProperty0<String>) = startStrField(text, prop.get())
        .setSaveConsumer { prop.set(it) }
        .build()
    fun ConfigEntryBuilder.mkIntField(text: Text, prop: KMutableProperty0<Int>) = startIntField(text, prop.get())
        .setSaveConsumer { prop.set(it) }
        .build()
}