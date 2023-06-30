package com.dulkirfabric.features

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.ClientTickEvent
import com.dulkirfabric.events.MouseScrollEvent
import meteordevelopment.orbit.EventHandler
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW
import kotlin.math.max

object InventoryScale {

    var scaleBuffer = DulkirConfig.configOptions.inventoryScale
    var prevTickScale = DulkirConfig.configOptions.inventoryScale
    var tickScale = DulkirConfig.configOptions.inventoryScale
    var frameScale = DulkirConfig.configOptions.inventoryScale


    /**
     * Called every render frame, so don't put anything expensive in here.
     */
    fun getScale(): Float {
        if (DulkirConfig.configOptions.invScaleBool && mc.currentScreen is HandledScreen<*>) {
            val partialTicks = MinecraftClient.getInstance().tickDelta
            DulkirConfig.configOptions.inventoryScale = prevTickScale + ((tickScale - prevTickScale) * partialTicks)
            return DulkirConfig.configOptions.inventoryScale
        }
        return 1f
    }

    @EventHandler
    fun onScroll(event: MouseScrollEvent) {
        // TODO: ignore input in config screen
        if (event.verticalScrollAmount == 0.0) return
        val handle = MinecraftClient.getInstance().window.handle
        if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_CONTROL) && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_ALT))
            scaleBuffer = max(scaleBuffer + (.05 * event.verticalScrollAmount).toFloat(), .1f)
    }

    @EventHandler
    fun onTick(event: ClientTickEvent) {
        // flushes the buffer to a scroll amount this tick, will be interpolated in calculatePos
        prevTickScale = tickScale
        tickScale = scaleBuffer
    }
}