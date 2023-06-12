package com.dulkirfabric.features

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.events.ClientTickEvent
import com.dulkirfabric.events.MouseScrollEvent
import meteordevelopment.orbit.EventHandler
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil
import org.joml.Vector2i
import org.joml.Vector2ic
import org.lwjgl.glfw.GLFW

object TooltipImpl {

    var scaleBuffer = 1f
    var horizontalBuffer = 0.0
    var verticalBuffer = 0.0

    var tickScale = 0f
    var tickHorizontal = 0
    var tickVertical = 0

    var prevTickX = 0
    var prevTickY = 0

    fun calculatePos(v: Vector2ic): Vector2ic {
        // calculate the position of the tooltip based on the scroll amount
        val partialTicks = MinecraftClient.getInstance().tickDelta
        val newVec = v.add(prevTickX + ((tickHorizontal - prevTickX) * partialTicks).toInt(),
            prevTickY + ((tickVertical - prevTickY) * partialTicks).toInt(), Vector2i())
        return newVec
    }

    fun applyScale() {

    }

    @EventHandler
    fun onTick(event: ClientTickEvent) {
        // flushes the buffer to a scroll amount this tick, will be interpolated in calculatePos
        prevTickX = tickHorizontal
        prevTickY = tickVertical
        tickHorizontal = horizontalBuffer.toInt()
        tickVertical = verticalBuffer.toInt()
        tickScale = scaleBuffer
    }

    @EventHandler
    fun onScroll(event: MouseScrollEvent) {
        if (event.verticalScrollAmount == 0.0) return
        val handle = MinecraftClient.getInstance().window.handle
        if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_SHIFT)) {
            horizontalBuffer += (mc.window.width / 192) * event.verticalScrollAmount
        } else if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_CONTROL)) {
            scaleBuffer += .1f * event.verticalScrollAmount.toFloat()
        } else {
            verticalBuffer += (mc.window.height / 108) * event.verticalScrollAmount
        }
    }
}