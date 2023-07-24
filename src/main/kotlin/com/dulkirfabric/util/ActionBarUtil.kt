package com.dulkirfabric.util

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.LongUpdateEvent
import com.dulkirfabric.events.chat.OverlayReceivedEvent
import com.dulkirfabric.util.ScoreBoardUtils.formattedString
import meteordevelopment.orbit.EventHandler
import net.minecraft.text.Text
import java.text.NumberFormat

object ActionBarUtil {

    private val splitRegex = " {2,}".toRegex()
    var healthStr = ""
    var defStr = ""
    var mana = ""
    var ehp = "§2?"
    var stacks = ""
    private val healthRegex = "([0-9,]+)/([0-9,]+)❤".toRegex()
    private var healthInt = 0
    private val defRegex = "([0-9,]+)❈ Defense".toRegex()
    private var defInt = 0

    @EventHandler
    fun onRenderActionBar(event: OverlayReceivedEvent) {
        if (!Utils.isInSkyblock()) return
        if (!DulkirConfig.configOptions.hudifyActionBar) return
        val sb = StringBuilder()
        var stackFlag = false
        event.message.formattedString().split(splitRegex).forEach {
            healthRegex.matchEntire(TextUtils.stripColorCodes(it))?.let { result ->
                healthInt = result.groupValues[1].replace(",", "").toInt()
                healthStr = it
                return@forEach
            }
            defRegex.matchEntire(TextUtils.stripColorCodes(it))?.let { result ->
                defInt = result.groupValues[1].replace(",", "").toInt()
                defStr = it.replace(" Defense", "")
                return@forEach
            }
            if (it.contains('ᝐ')) {
                stackFlag = true
                stacks = it
                return@forEach
            }
            if (it.contains('✎')) {
                mana = it
                return@forEach
            }
            sb.append(it)
        }
        if (!stackFlag) stacks = ""
        event.setReturnValue(Text.literal(sb.toString()))
    }

    @EventHandler
    fun onLongUpdate(event: LongUpdateEvent) {
        ehp = "§2${NumberFormat.getInstance().format((healthInt * (1 + (defInt / 100f))).toInt())}"
    }
}