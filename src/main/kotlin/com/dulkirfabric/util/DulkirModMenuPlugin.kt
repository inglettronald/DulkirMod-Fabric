package com.dulkirfabric.util

import com.dulkirfabric.config.DulkirConfig
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi

class DulkirModMenuPlugin : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { DulkirConfig().buildScreen(it) }
    }
}