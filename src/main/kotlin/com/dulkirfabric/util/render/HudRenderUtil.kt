package com.dulkirfabric.util.render

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.events.HudRenderEvent
import meteordevelopment.orbit.EventHandler
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import java.time.Duration

object HudRenderUtil {

    private var curTitle: Component? = null
    private var clearTime: Long = -1

    private fun drawTitle(context: GuiGraphics, content: Component) {
        val matrices = context.pose()
        val font = mc.font
        val w = font.width(content)
        val sf: Float = mc.window.guiScaledWidth / w.toFloat() / 3
        matrices.pushMatrix()
        matrices.translate(mc.window.guiScaledWidth / 3f, mc.window.guiScaledHeight / 2f)
        matrices.scale(sf, sf)
        context.drawString(font, content, 0, -font.lineHeight / 2, -1, true)
        matrices.popMatrix()
    }

    fun drawTitle(content: Component, duration: Duration) {
        curTitle = content
        clearTime = System.currentTimeMillis() + duration.toMillis()
    }

    @EventHandler
    fun onHudRender(event: HudRenderEvent) {
        val content = curTitle ?: return
        if (System.currentTimeMillis() >= clearTime) {
            curTitle = null
            return
        }
        drawTitle(event.context, content)
    }
}