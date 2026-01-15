package com.dulkirfabric.features.filters

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.ClientTickEvent
import com.dulkirfabric.util.TextUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.decoration.ArmorStand

object DamageNumbers {

    private val nonCritFormat = """^(\d{1,3}(,\d{3})*|\d+)$""".toRegex()
    private val trimPattern = "\\D".toRegex()
    private val critColorList = listOf(
        "§e", "§f", "§c", "§6"
    )
    @EventHandler
    fun worldLast(event: ClientTickEvent) {
        // TODO: this is terrible
        val ents = mc.level?.entitiesForRendering() ?: return
        ents.forEach {
            if (it !is ArmorStand) return@forEach
            if (!it.isMarker) return@forEach
            if (!it.hasCustomName()) return@forEach
            if (!it.isCustomNameVisible) return@forEach
            val name = it.customName?.string ?: return@forEach
            if (name matches nonCritFormat && DulkirConfig.configOptions.hideNonCrits) {
                it.isCustomNameVisible = false
                return@forEach
            }
            if (name.startsWith('✧')) {
                if (DulkirConfig.configOptions.hideCrits) {
                    it.isCustomNameVisible = false
                    return@forEach
                }
                if (!DulkirConfig.configOptions.truncateDamage) return@forEach
                val s = name.replace(trimPattern, "")
                try {
                    val critAmount = s.toInt()
                    it.customName = Component.literal(truncate(critAmount).applyDulkirCritColors())
                } catch (e: NumberFormatException) {
                    TextUtils.info("Error in Damage Truncation: $s", true)
                    TextUtils.info("Please report this on my discord!", true)
                }
            }
        }
    }

    /**
     * Truncate any positive int
     */
    private fun truncate(value: Int): String {
        val str = value.toString()
        return when {
            str.length > 9 -> {
                val i = str.length - 9
                "✧ ${str.substring(0, i)}.${str[i]}B ✧"
            }

            str.length > 6 -> {
                val i = str.length - 6
                "✧ ${str.substring(0, i)}.${str[i]}M ✧"
            }

            str.length > 3 -> {
                val i = str.length - 3
                "✧ ${str.substring(0, i)}.${str[i]}K✧"
            }

            else -> "✧ $str ✧"
        }
    }

    private fun String.applyDulkirCritColors(): String {
        val sb = StringBuilder()
        var i = 0
        for (char in this) {
            if (char != ' ') {
                sb.append(critColorList[i % critColorList.size])
                i++
            }
            sb.append(char)
        }
        return sb.toString()
    }
}
