package com.dulkirfabric.util.render

import java.awt.Color

interface GlowingEntityInterface {
    fun `dulkir$setEntityGlow`(shouldGlow: Boolean = false, glowColor: Color)

    fun `dulkir$shouldEntityGlow`() : Boolean

    fun `dulkir$getGlowColor`() : Color?
}