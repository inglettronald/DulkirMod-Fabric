package com.dulkirfabric.util.render

import com.dulkirfabric.util.JarvisIntegrationPlugin
import kotlinx.serialization.Serializable
import moe.nea.jarvis.api.JarvisHud
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.joml.Matrix3x2f
import org.joml.Vector2i
import org.joml.Vector2ic

class HudElement (var meta: HudMeta,
                  val identifier: Identifier,
                  private val label: Text,
                  private val width: Int,
                  private val height: Int
    ): JarvisHud, JarvisHud.Scalable {

    override fun getHudId(): Identifier {
        return this.identifier;
    }

    override fun getPosition(): Vector2ic {
        return Vector2i(this.meta.xPos, this.meta.yPos)
    }

    override fun setPosition(position: Vector2ic) {
        this.meta.xPos = position.x()
        this.meta.yPos = position.y()
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun isVisible(): Boolean {
        return true
    }

    override fun getUnscaledWidth(): Int {
        return this.width
    }

    override fun getUnscaledHeight(): Int {
        return this.height
    }

    override fun getLabel(): Text {
        return this.label
    }

    override fun getScale(): Float {
        return this.meta.scale
    }

    override fun setScale(scale: Float) {
        this.meta.scale = scale
    }

    @Serializable
    data class HudMeta(
        var xPos: Int = 1,
        var yPos: Int = 1,
        var scale: Float = 1f
    )

    fun applyTransformations(matrices: Matrix3x2f?) {
        applyTransformations(JarvisIntegrationPlugin.jarvis, matrices)
    }

}