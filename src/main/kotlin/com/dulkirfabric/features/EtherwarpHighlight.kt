package com.dulkirfabric.features

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.ClientTickEvent
import com.dulkirfabric.events.LongUpdateEvent
import com.dulkirfabric.events.WorldRenderLastEvent
import com.dulkirfabric.util.TextUtils
import com.dulkirfabric.util.Utils
import com.dulkirfabric.util.Utils.getBlockAt
import com.dulkirfabric.util.render.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.block.AbstractPressurePlateBlock
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import java.awt.Color

object EtherwarpHighlight {
    private var holdingEtherwarp = false

    @EventHandler
    fun onTick(event: ClientTickEvent) {
        holdingEtherwarp = mc.player?.mainHandStack?.isEtherwarp() ?: false
    }

    private fun ItemStack.isEtherwarp(): Boolean {
        val tag = Utils.getNbt(this) ?: return false
        if (tag.getBoolean("ethermerge", false)) return true
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
        if (!player.isSneaking) return

        // Find the targeted block with a range of 60.9
        val entity = mc.cameraEntity
        val blockHit = raycast(entity!!, 60.9, mc.renderTickCounter.getTickProgress(true))
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
        return !pos.isValidFeet() && pos.up().isValidFeet() && pos.up(2).isValidFeet()
    }

    @Suppress("DEPRECATION")
    private fun BlockPos.isValidFeet(): Boolean = getBlockAt().let { it.overrideValidFeet() ?: !it.isSolid }

    // Add blocks that dont follow the logic of isSolid for etherwarp to here
    private fun BlockState.overrideValidFeet(): Boolean? {
        return when (block) {
            is AbstractPressurePlateBlock -> false
            else -> null
        }
    }

    private fun raycast(entity: Entity, maxDistance: Double, tickDelta: Float): HitResult {
        // 1.7 if not crouch, 1.54 if crouch
        val crouching = mc.player!!.isSneaking
        val vec3d: Vec3d = mc.player!!.pos.add(0.0, if (crouching) 1.54 else 1.7, 0.0)
        val vec3d2: Vec3d = entity.getRotationVec(tickDelta)
        val vec3d3 = vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance)
        return mc.world!!
            .raycast(
                RaycastContext(
                    vec3d,
                    vec3d3,
                    RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.ANY,
                    entity
                )
            )
    }

    private fun err(): Boolean {
        TextUtils.info("§6Error in getting block info for AOTV highlight, please report.")
        return true
    }
}