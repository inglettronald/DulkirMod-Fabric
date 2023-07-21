package com.dulkirfabric.util

import kotlinx.serialization.Serializable

@Serializable
data class AnimationPreset(
    var posX: Int = 0,
    var posY: Int = 0,
    var posZ: Int = 0,
    var rotX: Int = 0,
    var rotY: Int = 0,
    var rotZ: Int = 0,
    var scale: Float = 1f,
    var swingDuration: Int = 6,
    var cancelReEquip: Boolean = false,
    var rotationlessDrink: Boolean = true,
)