package com.dulkirfabric.features

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.ClientTickEvent
import com.dulkirfabric.events.WorldRenderLastEvent
import com.dulkirfabric.util.Utils
import com.dulkirfabric.util.Utils.getBlockAt
import com.dulkirfabric.util.render.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.block.BasePressurePlateBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import java.awt.Color

object EtherwarpHighlight {
    private var holdingEtherwarp = false

    @EventHandler
    fun onTick(event: ClientTickEvent) {
        holdingEtherwarp = mc.player?.mainHandItem?.isEtherwarp() ?: false
    }

    private fun ItemStack.isEtherwarp(): Boolean {
        val tag = Utils.getNbt(this) ?: return false
        if (tag.getBoolean("ethermerge").orElse(false)) return true
        val id = tag.getString("id")
        return id.isPresent && id.get() == "ETHERWARP_CONDUIT"
    }

    /*@EventHandler
    fun onLong(event: LongUpdateEvent) {
        //println(heldItemID)º
    }*/

    @EventHandler
    fun onWorldRenderLast(event: WorldRenderLastEvent) {
        if (!DulkirConfig.configOptions.showEtherwarpPreview) return
        // check that holding etherwarp
        if (!holdingEtherwarp) return
        val player = mc.player ?: return
        if (!player.isCrouching) return

        // Find the targeted block with a range of 60.9
        val entity = mc.cameraEntity
        val blockHit = raycast(entity!!, 60.9, mc.deltaTracker.getGameTimeDeltaPartialTick(true))
        if (blockHit.type != HitResult.Type.BLOCK) return
        val pos: BlockPos = (blockHit as BlockHitResult).blockPos
        val isValid = isValidTeleportLocation(pos)
        val color = if (isValid) DulkirConfig.configOptions.etherwarpPreviewColor
        else DulkirConfig.configOptions.etherwarpInvalidPreviewColor

        // if found display box
        WorldRenderUtils.drawBox(
            event.context, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 1.0, 1.0, 1.0,
            Color(color, true), false
        )
    }

    private fun isValidTeleportLocation(pos: BlockPos): Boolean {
        return !pos.isValidFeet() && pos.above().isValidFeet() && pos.above(2).isValidFeet()
    }

    @Suppress("DEPRECATION")
    private fun BlockPos.isValidFeet(): Boolean = getBlockAt().let { it.overrideValidFeet() ?: !it.isSolid }

    // Add blocks that dont follow the logic of isSolid for etherwarp to here
    private fun BlockState.overrideValidFeet(): Boolean? {
        return when (block) {
            is BasePressurePlateBlock -> false
            else -> null
        }
    }

    private fun raycast(entity: Entity, maxDistance: Double, tickDelta: Float): HitResult {
        // 1.7 if not crouch, 1.54 if crouch
        val crouching = mc.player!!.isCrouching
        val vec3d: Vec3 = mc.player!!.position().add(0.0, if (crouching) 1.54 else 1.7, 0.0)
        val vec3d2: Vec3 = entity.getViewVector(tickDelta)
        val vec3d3 = vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance)
        return mc.level!!
            .clip(
                ClipContext(
                    vec3d,
                    vec3d3,
                    ClipContext.Block.OUTLINE,
                    ClipContext.Fluid.ANY,
                    entity
                )
            )
    }

}