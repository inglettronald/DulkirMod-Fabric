package com.dulkirfabric.features

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.PlaySoundEvent
import com.dulkirfabric.events.WorldLoadEvent
import com.dulkirfabric.util.SoundInfo
import com.dulkirfabric.util.TrackedCooldown
import com.dulkirfabric.util.Utils
import meteordevelopment.orbit.EventHandler
import net.minecraft.client.sound.Sound
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
        ),
        Pair(
            SoundInfo("block.lever.click", 2f, .55f),
            TrackedCooldown("RAGNAROCK_AXE".toRegex(), 20000, 0)
        ),
        Pair(
            SoundInfo("entity.generic.drink", 1.7936507f, 1f),
            TrackedCooldown("HOLY_ICE".toRegex(), 4000, 0)
        ),
        Pair(
            SoundInfo("entity.wolf.howl", 1.5238096f, .5f),
            TrackedCooldown("WEIRDER_TUBA".toRegex(), 20000, 0)
        ),
        Pair(
            SoundInfo("block.lava.pop", 2f, .4f),
            TrackedCooldown("ROGUE_SWORD".toRegex(), 30000, 0)
        ),
        Pair(
            SoundInfo("block.anvil.land", 0.4920635f, 1f),
            TrackedCooldown("GIANTS_SWORD".toRegex(), 30000, 0)
        )
    )

    private var lastRagTick: Long = 0

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
        val path = event.sound.id.path
        val pitch = event.sound.pitch
        val volume = event.sound.volume

        // Figure out if we have a match in trackedCooldowns
        val matchResult = trackedCooldowns[SoundInfo(path, pitch, volume)] ?: return

        if (matchResult.itemID matches "RAGNAROCK_AXE") {
            if (System.currentTimeMillis() - lastRagTick < 5000) {
                lastRagTick = System.currentTimeMillis()
                return
            } else {
                lastRagTick = System.currentTimeMillis()
            }
        }
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