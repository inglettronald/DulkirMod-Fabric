package com.dulkirfabric.features

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen

object InventoryScale {

    /**
     * Called every render frame, so don't put anything expensive in here.
     */
    fun getScale(): Float {
        if (!DulkirConfig.configOptions.invScaleBool) {
            return 1f
        }
        if (mc.screen !is AbstractContainerScreen<*>) {
            return 1f
        }
        return DulkirConfig.configOptions.inventoryScale
    }

}