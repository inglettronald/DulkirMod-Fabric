package com.dulkirfabric.events.chat

import com.dulkirfabric.events.base.Event

data class ModifyCommandEvent (
    var command: String,
): Event()