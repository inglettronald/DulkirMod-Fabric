package com.dulkirfabric.features

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.BlockOutlineEvent
import com.dulkirfabric.util.ColorUtil
import com.dulkirfabric.util.render.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.world.phys.AABB

object CustomBlockOutline {

    /**
     * Renders AABB of a block, not quite accurate to vanilla for shapes that are not rectangular,
     * but I can't be bothered to fix it.
     */
    @EventHandler
    fun onBlockOutline(event: BlockOutlineEvent) {
        if (!DulkirConfig.configOptions.customBlockOutlines) return
        val color = ColorUtil.toRGB(DulkirConfig.configOptions.blockOutlineColor)

        val context = event.blockOutlineContext;
        WorldRenderUtils.drawWireFrame(
            event.worldRenderContext, context.shape().bounds().move(context.pos()),
            color, 3f * DulkirConfig.configOptions.blockOutlineThickness, true
        )

        event.isCancelled = true
    }

}