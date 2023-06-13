package com.dulkirfabric.util

import java.awt.Color

object ColorUtil {
    private fun getRed (colorInt: Int): Int {
        return colorInt shr 16 and 255
    }

    private fun getGreen (colorInt: Int): Int {
        return colorInt shr 8 and 255
    }

    private fun getBlue (colorInt: Int): Int {
        return colorInt and 255
    }

    fun toRGB (colorInt: Int) : Color {
        return Color(getRed(colorInt), getGreen(colorInt), getBlue(colorInt))
    }
}