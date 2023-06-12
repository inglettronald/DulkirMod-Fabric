package com.dulkirfabric.features

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.events.ClientTickEvent
import com.dulkirfabric.events.MouseScrollEvent
import com.dulkirfabric.events.TooltipRenderChangeEvent
import meteordevelopment.orbit.EventHandler
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil
import net.minecraft.client.util.math.MatrixStack
import org.joml.Vector2i
import org.joml.Vector2ic
import org.lwjgl.glfw.GLFW

object TooltipImpl {

    private var scaleBuffer = 1f
    private var horizontalBuffer = 0.0
    private var verticalBuffer = 0.0
    private var tickScale = 0f
    private var tickHorizontal = 0
    private var tickVertical = 0
    private var prevTickX = 0
    private var prevTickY = 0
    private var prevScale = 1f
    private var frameScale = 1f
    private var frameX = 0
    private var frameY = 0

    fun calculatePos(v: Vector2ic): Vector2ic {
        // calculate the position of the tooltip based on the scroll amount
        val partialTicks = MinecraftClient.getInstance().tickDelta
        frameX = v.x() + prevTickX + ((tickHorizontal - prevTickX) * partialTicks).toInt()
        frameY = v.y() + prevTickY + ((tickVertical - prevTickY) * partialTicks).toInt()
        val newVec = v.add(-v.x() + frameX, -v.y() + frameY, Vector2i())
        return newVec
    }

    fun applyScale(matrices: MatrixStack) {
        frameScale = prevScale + (tickScale - prevScale) * MinecraftClient.getInstance().tickDelta
        matrices.scale(frameScale, frameScale, 1f)
        val newX = frameX / frameScale
        val newY = frameY / frameScale
        matrices.translate(newX - frameX, newY - frameY, 0f)
    }

    @EventHandler
    fun onTick(event: ClientTickEvent) {
        // flushes the buffer to a scroll amount this tick, will be interpolated in calculatePos
        prevTickX = tickHorizontal
        prevTickY = tickVertical
        prevScale = tickScale
        tickHorizontal = horizontalBuffer.toInt()
        tickVertical = verticalBuffer.toInt()
        tickScale = scaleBuffer
    }

    @EventHandler
    fun onScroll(event: MouseScrollEvent) {
        // TODO: ignore input in config screen
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

    @EventHandler
    fun onChange(event: TooltipRenderChangeEvent) {
        scaleBuffer = 1f
        horizontalBuffer = 0.0
        verticalBuffer = 0.0
        tickScale = 1f
        tickHorizontal = 0
        tickVertical = 0
        prevTickX = 0
        prevTickY = 0
        prevScale = 1f
    }
}