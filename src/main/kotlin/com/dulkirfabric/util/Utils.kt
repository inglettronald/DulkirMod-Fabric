package com.dulkirfabric.util

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.events.PlaySoundEvent
import com.dulkirfabric.events.SlayerBossEvents
import com.dulkirfabric.events.WorldLoadEvent
import com.dulkirfabric.events.chat.ChatEvents
import meteordevelopment.orbit.EventHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3

object Utils {

    private var inSlayerBoss = false

    fun isInSkyblock(): Boolean {
        return ScoreBoardUtils.getLines() != null
    }

    /**
     * Prints relevant information about a sound that is being displayed
     */
    fun debugSound(event: PlaySoundEvent) {
        if (event.sound.location.path == "entity.player.hurt") return
        println("Path: ${event.sound.location.path}")
        println("Pitch: ${event.sound.pitch}")
        println("Volume: ${event.sound.volume}")
    }

    private fun lerp(prev: Vec3, cur: Vec3, tickDelta: Float): Vec3 {
        return Vec3(
            prev.x + (cur.x - prev.x) * tickDelta,
            prev.y + (cur.y - prev.y) * tickDelta,
            prev.z + (cur.z - prev.z) * tickDelta,
        )
    }

    fun Entity.getInterpolatedPos(tickDelta: Float): Vec3 {
        val prevPos = Vec3(this.xo, this.yo, this.zo)
        return lerp(prevPos, this.position(), tickDelta)
    }

    fun BlockPos.getBlockAt() = mc.level!!.getBlockState(this)

    @EventHandler
    fun detectSlayerEvents(event: ChatEvents.AllowChat) {
        if (event.message.string.trim() == "SLAYER QUEST COMPLETE!") {
            SlayerBossEvents.Kill(ScoreBoardUtils.slayerType ?: return ScoreBoardUtils.err()).post()
        } else if (event.message.string.trim() == "SLAYER QUEST FAILED!") {
            SlayerBossEvents.Fail(ScoreBoardUtils.slayerType ?: return ScoreBoardUtils.err()).post()
        }
    }

    @EventHandler
    fun onSpawn(event: SlayerBossEvents.Spawn) {
        inSlayerBoss = true
    }

    @EventHandler
    fun onSwap(event: WorldLoadEvent) {
        if (inSlayerBoss)
            SlayerBossEvents.Fail(ScoreBoardUtils.slayerType).post()
    }

    @EventHandler
    fun onKill(event: SlayerBossEvents.Kill) {
        inSlayerBoss = false
    }

    @EventHandler
    fun onFail(event: SlayerBossEvents.Fail) {
        inSlayerBoss = false
    }

    fun getSkullTexture(itemStack: ItemStack): String? {
        return itemStack.components?.get(DataComponents.PROFILE)?.partialProfile?.properties?.get("textures")?.first()?.value
    }

    fun getNbt(itemStack: ItemStack): CompoundTag? {
        return itemStack.components?.get(DataComponents.CUSTOM_DATA)?.tag
    }
}