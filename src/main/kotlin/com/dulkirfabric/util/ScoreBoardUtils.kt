package com.dulkirfabric.util

import com.dulkirfabric.DulkirModFabric
import com.dulkirfabric.events.LongUpdateEvent
import com.dulkirfabric.events.PlaySoundEvent
import com.dulkirfabric.events.SlayerBossEvents
import meteordevelopment.orbit.EventHandler
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import net.minecraft.network.chat.Style
import net.minecraft.world.scores.PlayerTeam
import java.util.*

object ScoreBoardUtils {

    var hasActiveSlayerQuest = false
    var slayerType: String? = ""

    /**
     * Gets Scoreboard lines, will return null if not in Skyblock.
     */
    fun getLines(): MutableList<String>? {
        val scoreboard = DulkirModFabric.mc.level?.scoreboard ?: return null
        // This returns null if we're not in skyblock curiously
        val sidebarObjective = scoreboard.getObjective("SBScoreboard") ?: return null
        val scores = scoreboard.listPlayerScores(sidebarObjective)
        val lines: MutableList<String> = ArrayList()
        for (score in scores.reversed()) {
            val team = scoreboard.getPlayerTeam(score.owner)
            var str = PlayerTeam.formatNameForTeam(team, Component.literal(score.owner)).string
                .replace("§[^a-f0-9]".toRegex(), "")
            lines.add(str)
        }
        return lines
    }

    /**
     * This is useful for a few number of features in which you want to register the color of the scoreboard,
     * namely effigy display for now - but might be useful later? Who knows.
     */
    fun getLinesWithColor(): MutableList<String>? {
        val scoreboard = DulkirModFabric.mc.level?.scoreboard ?: return null
        // This returns null if we're not in skyblock curiously
        val sidebarObjective = scoreboard.getObjective("SBScoreboard") ?: return null
        val scores = scoreboard.listPlayerScores(sidebarObjective)
        val lines: MutableList<String> = ArrayList()
        for (score in scores.reversed()) {
            val team = scoreboard.getPlayerTeam(score.owner)
            lines.add(PlayerTeam.formatNameForTeam(team, Component.literal(score.owner)).formattedString())
        }
        return lines
    }

    /**
     * Function to add 1.8.9 Style color coding to strings, if you wish. Helpful for porting some 1.8.9 features
     * to newer versions.
     *
     * @author nea
     */
    fun Component.formattedString(): String {
        val sb = StringBuilder()
        visit(FormattedText.StyledContentConsumer<Unit> { style, string ->
            val c = ChatFormatting.getByName(style.color?.name)
            if (c != null) {
                sb.append("§${c.code}")
            }
            if (style.isUnderlined) {
                sb.append("§n")
            }
            if (style.isBold) {
                sb.append("§l")
            }
            sb.append(string)
            Optional.empty()
        }, Style.EMPTY)
        return sb.toString().replace("§[^a-f0-9]".toRegex(), "")
    }

    @EventHandler
    fun updateUtility(event: LongUpdateEvent) {
        val lines = getLines() ?: return
        lines.forEach {
            when {
                it.contains("Slayer Quest") -> {
                    hasActiveSlayerQuest = true
                    val i = lines.indexOf(it) + 1
                    if (i == 0) return err()
                    slayerType = lines[i]
                }
            }
        }
    }

    @EventHandler
    fun onSound(event: PlaySoundEvent) {
        if (event.sound.location.path != "entity.wither.shoot") return
        if (event.sound.pitch != 0.6984127f) return
        if (event.sound.volume != .5f) return
        SlayerBossEvents.Spawn(slayerType ?: return err()).post()
    }

    fun err() {
        TextUtils.info("§6Error Determining active slayer type, please report.")
    }
}