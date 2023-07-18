package com.dulkirfabric.util

import java.awt.Color

interface GlowingEntityInterface {
    fun setDulkirEntityGlow(shouldGlow: Boolean = false, glowColor: Color, shouldESP: Boolean = false)

    fun shouldDulkirEntityGlow() : Boolean

    fun getDulkirEntityGlowColor() : Color?

    fun shouldDulkirEntityESP() : Boolean
}