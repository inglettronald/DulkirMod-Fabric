package com.dulkirfabric.util

import com.dulkirfabric.events.PlaySoundEvent
import com.dulkirfabric.events.SlayerBossEvents
import com.dulkirfabric.events.WorldLoadEvent
import com.dulkirfabric.events.chat.ChatEvents
import meteordevelopment.orbit.EventHandler
import net.minecraft.block.entity.SkullBlockEntity
import net.minecraft.client.texture.PlayerSkinProvider
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtHelper
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
        val prevPos = Vec3d(this.prevX, this.prevY, this.prevZ)
        return lerp(prevPos, this.pos, tickDelta)
    }

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
        return itemStack.getSubNbt(SkullBlockEntity.SKULL_OWNER_KEY)?.let(NbtHelper::toGameProfile)?.properties?.get(
            PlayerSkinProvider.TEXTURES)?.first()?.value
    }
}