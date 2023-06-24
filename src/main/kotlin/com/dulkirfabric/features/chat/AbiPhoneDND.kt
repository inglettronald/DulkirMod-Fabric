package com.dulkirfabric.features.chat

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.ChatReceivedEvent
import com.dulkirfabric.events.OverlayReceivedEvent
import com.dulkirfabric.events.PlaySoundEvent
import com.dulkirfabric.util.TextUtils
import com.dulkirfabric.util.TextUtils.unformattedString
import meteordevelopment.orbit.EventHandler
import net.minecraft.client.sound.Sound
import net.minecraft.text.Text

object AbiPhoneDND {

    private val abiPhoneFormat = "✆ (\\w+) ✆ ".toRegex()
    private var lastRing = 0L

    //BLOCK ABIPHONE SOUNDS
    @EventHandler
    fun onSound(event: PlaySoundEvent) {
        if (!DulkirConfig.configOptions.abiPhoneDND) return
        if (System.currentTimeMillis() - lastRing < 5000) {
            // TODO: Make this not error out madge
            if (event.sound == null) {
                event.isCancelled = true
            }

            if (event.sound.id.path == "block.note_block.pling" && event.sound.volume == 0.69f && event.sound.pitch == 1.6666666f) {
                    event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun handle(event: ChatReceivedEvent) {
        if (!DulkirConfig.configOptions.abiPhoneDND) return
        val unformatted: String = event.message.unformattedString
        println(unformatted)
        if (unformatted matches abiPhoneFormat && !unformatted.contains("Elle") && !unformatted.contains("Dean")) {
            val matchResult = abiPhoneFormat.find(unformatted)
            event.isCancelled = true
            lastRing = System.currentTimeMillis()
            if (DulkirConfig.configOptions.abiPhoneCallerID) {
                val blocked = if (Math.random() < .001) "Breefing"
                else matchResult?.groups?.get(1)?.value
                TextUtils.info("§6Call blocked from $blocked!")
            }
        }
        if (unformatted.startsWith("✆ Ring...") && unformatted.endsWith("[PICK UP]")
            && System.currentTimeMillis() - lastRing < 5000) {
            event.isCancelled = true
        }
    }
}