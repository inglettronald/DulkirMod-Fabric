package com.dulkirfabric.hud

import com.dulkirfabric.DulkirModFabric
import com.dulkirfabric.config.DulkirConfig
import moe.nea.jarvis.api.Jarvis
import moe.nea.jarvis.api.JarvisConfigOption
import moe.nea.jarvis.api.JarvisHud
import moe.nea.jarvis.api.JarvisPlugin

class JarvisIntegration : JarvisPlugin {
    override fun getModId(): String =
        DulkirModFabric.modId

    companion object {
        lateinit var jarvis: Jarvis
    }

    override fun onInitialize(jarvis: Jarvis) {
        Companion.jarvis = jarvis
    }

    override fun getAllHuds(): List<JarvisHud> {
        return DulkirConfig.ConfigVars.huds.map { it -> it.first };
    }

    override fun onHudEditorClosed() {
        return DulkirConfig.ConfigVars.saveConfig()
    }

    override fun getAllConfigOptions(): List<JarvisConfigOption> {
        return listOf() // todo, dont understand the jumpTo function
    }

}