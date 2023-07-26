package com.dulkirfabric.util

import com.dulkirfabric.config.DulkirConfig
import moe.nea.jarvis.api.JarvisHud
import moe.nea.jarvis.api.JarvisPlugin

class JarvisIntegrationPlugin: JarvisPlugin {
    override fun getModId(): String {
        return "dulkirmod-fabric"
    }

    override fun getAllHuds(): List<JarvisHud> {
        return DulkirConfig.huds.map { it.first }
    }

    override fun onHudEditorClosed() {
        super.onHudEditorClosed()
        DulkirConfig.saveConfig()
    }
}