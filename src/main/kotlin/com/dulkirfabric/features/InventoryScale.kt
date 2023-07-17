package com.dulkirfabric.features

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import net.minecraft.client.gui.screen.ingame.HandledScreen

object InventoryScale {

    /**
     * Called every render frame, so don't put anything expensive in here.
     */
    fun getScale(): Float {
        if (DulkirConfig.configOptions.invScaleBool && mc.currentScreen is HandledScreen<*>) {
            return DulkirConfig.configOptions.inventoryScale
        }
        return 1f
    }

}