package com.dulkirfabric.util

object Utils {
    fun isInSkyblock(): Boolean {
        return ScoreBoardUtils.getLines() != null
    }
}