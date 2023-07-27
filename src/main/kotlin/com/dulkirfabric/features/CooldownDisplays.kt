package com.dulkirfabric.features

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.PlaySoundEvent
import com.dulkirfabric.events.WorldLoadEvent
import com.dulkirfabric.util.SoundInfo
import com.dulkirfabric.util.TrackedCooldown
import com.dulkirfabric.util.Utils
import meteordevelopment.orbit.EventHandler
import net.minecraft.item.ItemStack
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import kotlin.math.round

object CooldownDisplays {
    private val trackedCooldowns: Map<SoundInfo, TrackedCooldown> = mapOf(
        Pair(
            SoundInfo("entity.zombie_villager.cure", 1f, .5f),
            TrackedCooldown("(REAPER_CHESTPLATE)|(REAPER_LEGGINGS)|(REAPER_BOOTS)".toRegex(), 25000, 0)
        ),
        Pair(
            SoundInfo("entity.zombie_villager.cure", 0.6984127f, 1f),
            TrackedCooldown("(HYPERION|ASTRAEA|SCYLLA|VALKYRIE)".toRegex(), 5000, 0)
        ),
        Pair(
            SoundInfo("entity.firework_rocket.launch", 1f, 3f),
            TrackedCooldown("SOS_FLARE".toRegex(), 20000, 0)
        )
    )

    fun shouldDisplay(stack: ItemStack, cir: CallbackInfoReturnable<Boolean>) {
        val cooldown = fetchCooldownItem(stack) ?: return
        cir.returnValue = System.currentTimeMillis() - cooldown.lastUsage < cooldown.cooldownDuration
    }

    fun calcDurability(stack: ItemStack, cir: CallbackInfoReturnable<Int>) {
        val cooldown = fetchCooldownItem(stack) ?: return
        cir.returnValue = round(13f * (System.currentTimeMillis() - cooldown.lastUsage) / cooldown.cooldownDuration).toInt()
    }
    @EventHandler
    fun onSound(event: PlaySoundEvent) {
        if (!DulkirConfig.configOptions.duraCooldown) return
        // Utils.debugSound(event)
        val path = event.sound.id.path
        val pitch = event.sound.pitch
        val volume = event.sound.volume

        // Figure out if we have a match in trackedCooldowns
        val matchResult = trackedCooldowns[SoundInfo(path, pitch, volume)] ?: return
        matchResult.lastUsage = System.currentTimeMillis()
    }

    @EventHandler
    fun onWorldLoad(event: WorldLoadEvent) {
        if (!DulkirConfig.configOptions.duraCooldown) return
        trackedCooldowns.forEach {
            it.value.lastUsage = 0
        }
    }

    private fun fetchCooldownItem(stack: ItemStack): TrackedCooldown? {
        val tag = stack.nbt ?: return null
        val id = tag.getCompound("ExtraAttributes").get("id") ?: return null
        val idStr = id.toString().trim('"')
        trackedCooldowns.forEach {
            if (idStr matches it.value.itemID)
                return it.value
        }
        return null
    }
}