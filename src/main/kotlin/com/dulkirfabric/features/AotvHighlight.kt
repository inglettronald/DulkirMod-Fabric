package com.dulkirfabric.features

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.events.ClientTickEvent
import com.dulkirfabric.events.LongUpdateEvent
import com.dulkirfabric.events.WorldRenderLastEvent
import com.dulkirfabric.util.TextUtils
import com.dulkirfabric.util.render.WorldRenderUtils
import meteordevelopment.orbit.EventHandler
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil
import net.minecraft.entity.Entity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import org.lwjgl.glfw.GLFW
import java.awt.Color

object AotvHighlight {
    private var heldItemID = ""

    @EventHandler
    fun onTick(event: ClientTickEvent) {
        heldItemID = getHeldItemID()
    }

    fun getHeldItemID(): String {
        val stack = mc.player?.mainHandStack ?: return ""
        val tag = stack.nbt ?: return ""
        val id = tag.getCompound("ExtraAttributes").get("id") ?: return ""
        return id.toString().trim('"')
    }

    @EventHandler
    fun onLong(event: LongUpdateEvent) {
        //println(heldItemID)
    }

    @EventHandler
    fun onWorldRenderLast(event: WorldRenderLastEvent) {
        if (!DulkirConfig.configOptions.showEtherwarpPreview) return
        // check that holding aotv
        if (heldItemID != "ASPECT_OF_THE_VOID") return
        val handle = MinecraftClient.getInstance().window.handle
        if (!InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_SHIFT) || mc.currentScreen != null) return

        // Find the targeted block with a range of 60.9
        val entity = mc.cameraEntity
        if (mc.player == null) return
        val blockHit = raycast(entity!!, 60.9, mc.tickDelta)
        if (blockHit.type != HitResult.Type.BLOCK) return
        val pos: BlockPos = (blockHit as BlockHitResult).blockPos
        if (!isValidTeleportLocation(pos)) return

        // if found display box
        WorldRenderUtils.drawBox(event.context, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 1.0, 1.0, 1.0,
            Color(DulkirConfig.configOptions.etherwarpPreviewColor, true), false)
    }

    private fun isValidTeleportLocation(pos: BlockPos): Boolean {
        // TODO: Implement this (LOTS OF CASES, seems annoying)
        return true
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