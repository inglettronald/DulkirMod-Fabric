package com.dulkirfabric.util

import com.dulkirfabric.DulkirModFabric.mc
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

object HudRenderUtil {
    fun drawTitle(context: DrawContext, content: Text) {
        val matrices = context.matrices
        val tr = mc.textRenderer
        val w = tr.getWidth(content)
        val sf: Float = mc.window.scaledWidth / w.toFloat() / 3
        matrices.push()
        matrices.translate(mc.window.scaledWidth / 3f, mc.window.scaledHeight / 2f, 0f)
        matrices.scale(sf, sf, 1f)
        context.drawText(tr, content, 0, -tr.fontHeight / 2, -1, true)
        matrices.pop()
    }
}