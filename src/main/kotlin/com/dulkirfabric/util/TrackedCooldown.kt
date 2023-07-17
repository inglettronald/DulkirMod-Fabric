package com.dulkirfabric.util

// Currently, this will be used to create A list of items that have audible sounds associated with their usages
data class TrackedCooldown (
    val itemID: Regex,
    val cooldownDuration: Int, // in millis
    var lastUsage: Long, // from System.currentTimeMillis
)