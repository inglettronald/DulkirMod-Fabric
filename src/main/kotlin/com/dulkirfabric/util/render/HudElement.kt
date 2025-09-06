package com.dulkirfabric.util.render

import com.dulkirfabric.hud.JarvisIntegration
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

    override fun getHudId(): net.minecraft.class_2960 {
        return this.identifier;
    }

    override fun getPosition(): Vector2ic {
        return this.meta.position
    }

    override fun setPosition(position: Vector2ic?) {
        this.meta.position = position ?: Vector2i(0, 0)
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

    override fun getLabel(): net.minecraft.class_2561 {
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
        var position: Vector2ic,
        var scale: Float
    )

    fun applyTransformations(matrices: Matrix3x2f?) {
        applyTransformations(JarvisIntegration.jarvis, matrices)
    }

}