package com.dulkirfabric.util

import java.awt.Color

interface GlowingEntityInterface {
    fun setDulkirEntityGlow(shouldGlow: Boolean = false, glowColor: Color)

    fun shouldDulkirEntityGlow() : Boolean

    fun getDulkirEntityGlowColor() : Color?
}