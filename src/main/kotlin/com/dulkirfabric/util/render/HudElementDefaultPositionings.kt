package com.dulkirfabric.util.render

import kotlinx.serialization.Serializable

@Serializable
data class HudElementDefaultPositionings(
    var fooPos: HudElement.Positioning = HudElement.Positioning(.1, .1, 1f)
)
