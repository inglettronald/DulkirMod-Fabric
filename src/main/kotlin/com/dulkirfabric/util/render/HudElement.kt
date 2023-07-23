package com.dulkirfabric.util.render

import kotlinx.serialization.Serializable
import moe.nea.jarvis.api.JarvisHud
import moe.nea.jarvis.api.JarvisScalable
import net.minecraft.text.Text

class HudElement (var positioning: Positioning,
                  val key: String,
                  private val label: Text,
                  private val width: Int,
                  private val height: Int
    ): JarvisHud, JarvisScalable {

    @Serializable
    data class Positioning(
        var x: Double,
        var y: Double,
        var scale: Float
    )

    override fun getX(): Double {
        return positioning.x
    }

    override fun setX(newX: Double) {
        positioning.x = newX
    }

    override fun getY(): Double {
        return positioning.y
    }

    override fun setY(newY: Double) {
        positioning.y = newY
    }

    override fun getLabel(): Text {
        return label
    }

    override fun getWidth(): Int {
        return width
    }

    override fun getHeight(): Int {
        return height
    }

    override fun getScale(): Float {
        return positioning.scale
    }

    override fun setScale(newScale: Float) {
        positioning.scale = newScale
    }

}