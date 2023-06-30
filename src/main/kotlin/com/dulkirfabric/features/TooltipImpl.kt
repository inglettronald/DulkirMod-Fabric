package com.dulkirfabric.features

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
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
import kotlin.math.max

object TooltipImpl {

    private var scaleBuffer = DulkirConfig.configOptions.tooltipScale
    private var horizontalBuffer = 0.0
    private var verticalBuffer = 0.0
    private var tickScale = 0f
    private var tickHorizontal = 0
    private var tickVertical = 0
    private var prevTickX = 0
    private var prevTickY = 0
    private var prevScale = DulkirConfig.configOptions.tooltipScale
    private var frameScale = DulkirConfig.configOptions.tooltipScale
    private var frameX = 0
    private var frameY = 0

    fun calculatePos(v: Vector2ic, tw: Int, th: Int, sw: Int, sh: Int): Vector2ic {
        // calculate the position of the tooltip based on the scroll amount
        val partialTicks = MinecraftClient.getInstance().tickDelta
        var newVec = v
        frameX = newVec.x() + prevTickX + ((tickHorizontal - prevTickX) * partialTicks).toInt()
        frameY = newVec.y() + prevTickY + ((tickVertical - prevTickY) * partialTicks).toInt()
        frameScale = prevScale + (tickScale - prevScale) * partialTicks
        // Check for tooltips that go off both sides of screen
        if (tw > sw) {
           frameX = frameX - v.x() + 4
        }
        if (th > sh) {
            frameY = frameY - v.y() + 4
        }
        return Vector2i(0,0)
    }

    fun applyScale(matrices: MatrixStack) {
        matrices.translate(frameX.toFloat(), frameY.toFloat(), 0f)
        matrices.scale(frameScale, frameScale, 1f)
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
            scaleBuffer = max(.01f, scaleBuffer + .1f * event.verticalScrollAmount.toFloat())
        } else {
            verticalBuffer += (mc.window.height / 108) * event.verticalScrollAmount
        }
    }

    @EventHandler
    fun onChange(event: TooltipRenderChangeEvent) {
        scaleBuffer = DulkirConfig.configOptions.tooltipScale
        horizontalBuffer = 0.0
        verticalBuffer = 0.0
        tickScale = DulkirConfig.configOptions.tooltipScale
        tickHorizontal = 0
        tickVertical = 0
        prevTickX = 0
        prevTickY = 0
        prevScale = DulkirConfig.configOptions.tooltipScale
    }
}