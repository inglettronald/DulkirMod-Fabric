package com.dulkirfabric.features

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.BlockOutlineEvent
import com.dulkirfabric.util.ColorUtil
import com.dulkirfabric.util.render.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.util.math.Box

object CustomBlockOutline {

    /**
     * This is kind of a lazy implementation, but it works.
     * Notes: This always draws a 1x1x1 box around the block you're looking at. This is not accurate for all blocks,
     * because not all blocks have a 1x1x1 bounding box. It will still tell you the correct block you're looking at correctly.
     */
    @EventHandler
    fun onBlockOutline(event: BlockOutlineEvent) {
        if (!DulkirConfig.configOptions.customBlockOutlines) return
        val blockPos = event.blockOutlineContext.blockPos()
        val x = blockPos.x.toDouble()
        val y = blockPos.y.toDouble()
        val z = blockPos.z.toDouble()
        val color = ColorUtil.toRGB(DulkirConfig.configOptions.blockOutlineColor)

        WorldRenderUtils.drawWireFrame(
            event.worldRenderContext, Box(x, y, z, x + 1, y + 1, z + 1),
            color, 3f * DulkirConfig.configOptions.blockOutlineThickness, true
        )
        event.isCancelled = true
    }
}