package com.dulkirfabric.features

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.AreaChangeEvent
import com.dulkirfabric.events.LongUpdateEvent
import com.dulkirfabric.util.TablistUtils
import com.dulkirfabric.util.render.HudRenderUtil
import meteordevelopment.orbit.EventHandler
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import java.time.Duration

object VisitorAlert {

    private var canAlert = true
    private var prevVisitorState = ""

    @EventHandler
    private fun onAreaChange(event: AreaChangeEvent) {
        canAlert = true
    }

    @EventHandler
    private fun onLong(event: LongUpdateEvent) {
        if (!DulkirConfig.configOptions.visitorAlert || TablistUtils.persistentInfo.area != "Garden") return
        if (TablistUtils.persistentInfo.nextVisitorTime == "Queue Full!") {
            if (DulkirConfig.configOptions.persistentVisitorAlert) {
                HudRenderUtil.drawTitle(
                    Component.literal("Max Visitors").withStyle(ChatFormatting.GOLD),
                    Duration.ofSeconds(5)
                )
            } else if (canAlert) {
                HudRenderUtil.drawTitle(
                    Component.literal("Max Visitors").withStyle(ChatFormatting.GOLD),
                    Duration.ofSeconds(5)
                )
                canAlert = false
            }
        }

        prevVisitorState = TablistUtils.persistentInfo.nextVisitorTime
    }
}