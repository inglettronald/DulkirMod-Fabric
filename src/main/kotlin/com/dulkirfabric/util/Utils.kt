package com.dulkirfabric.util

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.events.PlaySoundEvent
import com.dulkirfabric.events.SlayerBossEvents
import com.dulkirfabric.events.WorldLoadEvent
import com.dulkirfabric.events.chat.ChatEvents
import meteordevelopment.orbit.EventHandler
import net.minecraft.block.entity.SkullBlockEntity
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtHelper
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

object Utils {

    private var inSlayerBoss = false

    fun isInSkyblock(): Boolean {
        return ScoreBoardUtils.getLines() != null
    }

    /**
     * Prints relevant information about a sound that is being displayed
     */
    fun debugSound(event: PlaySoundEvent) {
        if (event.sound.id.path == "entity.player.hurt") return
        println("Path: ${event.sound.id.path}")
        println("Pitch: ${event.sound.pitch}")
        println("Volume: ${event.sound.volume}")
    }

    private fun lerp(prev: Vec3d, cur: Vec3d, tickDelta: Float): Vec3d {
        return Vec3d(
            prev.x + (cur.x - prev.x) * tickDelta,
            prev.y + (cur.y - prev.y) * tickDelta,
            prev.z + (cur.z - prev.z) * tickDelta,
        )
    }
    fun Entity.getInterpolatedPos(tickDelta: Float): Vec3d {
        val prevPos = Vec3d(this.lastX, this.lastY, this.lastZ)
        return lerp(prevPos, this.pos, tickDelta)
    }

    fun BlockPos.getBlockAt() = mc.world!!.getBlockState(this)

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
        return itemStack.components?.get(DataComponentTypes.PROFILE)?.properties?.get("textures")?.first()?.value
    }

    // TODO: Don't use the NBT in favor of a better option
    fun getNbt(itemStack: ItemStack): NbtCompound? {
        return itemStack.components?.get(DataComponentTypes.CUSTOM_DATA)?.nbt
    }
}