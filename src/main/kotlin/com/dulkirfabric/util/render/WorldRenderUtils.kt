package com.dulkirfabric.util.render

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.events.HudRenderEvent
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.PoseStack
import meteordevelopment.orbit.EventHandler
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.joml.Quaternionf
import org.joml.Vector3f
import java.awt.Color

object WorldRenderUtils {

    // --- Pending 2D labels: queued during world render, flushed to HUD during onHudRender ---

    private data class PendingLabel(
        val text: Component,
        val distText: Component?,
        val screenX: Float,
        val screenY: Float,
        val textColor: Int,
        val backgroundColor: Int
    )

    private val pendingLabels = mutableListOf<PendingLabel>()

    @EventHandler
    fun onHudRender(event: HudRenderEvent) {
        val guiGraphics = event.context
        val font = mc.font
        for (label in pendingLabels) {
            val x = label.screenX.toInt()
            val y = label.screenY.toInt()
            val mainW = font.width(label.text)
            val distW = if (label.distText != null) font.width(label.distText) else 0
            val totalW = maxOf(mainW, distW)
            val totalH = if (label.distText != null) 19 else 9
            guiGraphics.fill(x - totalW / 2 - 1, y - 1, x + totalW / 2 + 1, y + totalH, label.backgroundColor)
            val textColor = label.textColor or 0xFF000000.toInt()
            guiGraphics.drawString(font, label.text, x - mainW / 2, y, textColor, true)
            if (label.distText != null) {
                guiGraphics.drawString(font, label.distText, x - distW / 2, y + 10, textColor, true)
            }
        }
        pendingLabels.clear()
    }

    /**
     * Projects a world-space position to GUI-scaled screen coordinates.
     * Returns null if behind the camera or off-screen.
     */
    private fun worldToScreen(pos: Vec3): Pair<Float, Float>? {
        val camera = mc.gameRenderer.mainCamera
        val camPos = camera.position()

        // World-relative offset
        val rel = Vector3f(
            (pos.x - camPos.x).toFloat(),
            (pos.y - camPos.y).toFloat(),
            (pos.z - camPos.z).toFloat()
        )

        // camera.rotation() is camera→world; conjugate is world→camera
        Quaternionf(camera.rotation()).conjugate().transform(rel)

        // In OpenGL camera space, forward is -Z. rel.z >= 0 means behind the camera.
        if (rel.z >= 0f) return null

        // Manual perspective projection using actual game FOV (avoids getProjectionMatrix(fov) ambiguity)
        val fovRad = Math.toRadians(mc.options.fov.get().toDouble()).toFloat()
        val aspect = mc.window.width.toFloat() / mc.window.height.toFloat()
        val tanHalfFovY = Math.tan((fovRad / 2.0)).toFloat()
        val ndcX = rel.x / (-rel.z * tanHalfFovY * aspect)
        val ndcY = rel.y / (-rel.z * tanHalfFovY)

        if (ndcX < -1.05f || ndcX > 1.05f || ndcY < -1.05f || ndcY > 1.05f) return null

        val guiW = mc.window.guiScaledWidth.toFloat()
        val guiH = mc.window.guiScaledHeight.toFloat()
        return Pair(
            (ndcX + 1f) * 0.5f * guiW,
            (1f - ndcY) * 0.5f * guiH
        )
    }

    // Renders a line as a camera-facing quad so thickness is actually respected.
    private fun thickLine(
        matrix: PoseStack.Pose, buffer: BufferBuilder,
        x1: Number, y1: Number, z1: Number,
        x2: Number, y2: Number, z2: Number,
        color: Int, halfWidth: Float,
        camX: Double, camY: Double, camZ: Double
    ) {
        val fx = x1.toFloat(); val fy = y1.toFloat(); val fz = z1.toFloat()
        val tx = x2.toFloat(); val ty = y2.toFloat(); val tz = z2.toFloat()

        val dir = Vector3f(tx - fx, ty - fy, tz - fz)
        if (dir.lengthSquared() < 1e-6f) return
        dir.normalize()

        val midX = (fx + tx) * 0.5f
        val midY = (fy + ty) * 0.5f
        val midZ = (fz + tz) * 0.5f
        val toCamera = Vector3f(camX.toFloat() - midX, camY.toFloat() - midY, camZ.toFloat() - midZ)
        if (toCamera.lengthSquared() < 1e-6f) return

        val perp = dir.cross(toCamera, Vector3f())
        if (perp.lengthSquared() < 1e-6f) return
        perp.normalize().mul(halfWidth)

        val mat = matrix.pose()
        buffer.addVertex(mat, fx - perp.x, fy - perp.y, fz - perp.z).setColor(color)
        buffer.addVertex(mat, fx + perp.x, fy + perp.y, fz + perp.z).setColor(color)
        buffer.addVertex(mat, tx + perp.x, ty + perp.y, tz + perp.z).setColor(color)
        buffer.addVertex(mat, tx - perp.x, ty - perp.y, tz - perp.z).setColor(color)
    }

    private fun addBoxVertices(
        matrix: PoseStack.Pose, buf: BufferBuilder,
        x1: Double, y1: Double, z1: Double,
        x2: Double, y2: Double, z2: Double,
        color: Int
    ) {
        val mat = matrix.pose()
        fun v(x: Double, y: Double, z: Double) {
            buf.addVertex(mat, x.toFloat(), y.toFloat(), z.toFloat()).setColor(color)
        }
        v(x1, y1, z1); v(x1, y1, z1)
        v(x1, y1, z2)
        v(x1, y2, z1)
        v(x1, y2, z2)
        v(x2, y2, z1)
        v(x2, y2, z2)
        v(x2, y1, z1)
        v(x2, y1, z2)
        v(x1, y1, z1)
        v(x1, y1, z2)
        v(x2, y1, z2)
        v(x1, y2, z2)
        v(x2, y2, z2)
    }

    fun drawWireFrame(
        context: WorldRenderContext,
        box: AABB,
        color: Color,
        thickness: Float,
        depthTest: Boolean = true
    ) {
        val matrices = context.matrices() ?: return
        matrices.pushPose()
        val layer = if (depthTest) DulkirRenderTypes.DULKIR_QUADS else DulkirRenderTypes.DULKIR_QUADS_ESP
        val camera = context.gameRenderer().mainCamera
        val camPos = camera.position()
        matrices.translate(-camPos.x, -camPos.y, -camPos.z)
        val buf = RenderUtil.getBufferFor(layer)
        val me = matrices.last()
        val argb = color.rgb
        val hw = thickness * 0.002f

        fun seg(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) =
            thickLine(me, buf, x1, y1, z1, x2, y2, z2, argb, hw, camPos.x, camPos.y, camPos.z)

        // bottom
        seg(box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ)
        seg(box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ)
        seg(box.maxX, box.minY, box.maxZ, box.minX, box.minY, box.maxZ)
        seg(box.minX, box.minY, box.maxZ, box.minX, box.minY, box.minZ)
        // verticals
        seg(box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ)
        seg(box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ)
        seg(box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ)
        seg(box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ)
        // top
        seg(box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ)
        seg(box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ)
        seg(box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ)
        seg(box.minX, box.maxY, box.maxZ, box.minX, box.maxY, box.minZ)

        layer.draw(buf.buildOrThrow())
        matrices.popPose()
    }

    fun drawLine(context: WorldRenderContext, startPos: Vec3, endPos: Vec3, color: Color, thickness: Float, depthTest: Boolean = true) {
        val matrices = context.matrices() ?: return
        matrices.pushPose()
        val layer = if (depthTest) DulkirRenderTypes.DULKIR_QUADS else DulkirRenderTypes.DULKIR_QUADS_ESP
        val camera = context.gameRenderer().mainCamera
        val camPos = camera.position()
        matrices.translate(-camPos.x, -camPos.y, -camPos.z)
        val buf = RenderUtil.getBufferFor(layer)
        val me = matrices.last()

        thickLine(me, buf, startPos.x, startPos.y, startPos.z, endPos.x, endPos.y, endPos.z, color.rgb, thickness * 0.002f, camPos.x, camPos.y, camPos.z)

        layer.draw(buf.buildOrThrow())
        matrices.popPose()
    }

    fun drawLineArray(context: WorldRenderContext, posArr: List<Vec3>, color: Color, thickness: Float,
                      depthTest: Boolean = true) {
        val matrices = context.matrices() ?: return
        matrices.pushPose()
        val layer = if (depthTest) DulkirRenderTypes.DULKIR_QUADS else DulkirRenderTypes.DULKIR_QUADS_ESP
        val camera = context.gameRenderer().mainCamera
        val camPos = camera.position()
        matrices.translate(-camPos.x, -camPos.y, -camPos.z)
        val buf = RenderUtil.getBufferFor(layer)
        val me = matrices.last()

        val argb = color.rgb
        val hw = thickness * 0.002f
        for (i in 0 until posArr.size - 1) {
            val startPos = posArr[i]
            val endPos = posArr[i + 1]
            thickLine(me, buf, startPos.x, startPos.y, startPos.z, endPos.x, endPos.y, endPos.z, argb, hw, camPos.x, camPos.y, camPos.z)
        }

        layer.draw(buf.buildOrThrow())
        matrices.popPose()
    }

    /**
     * Queues a world-space text label to be rendered in the HUD pass.
     */
    fun drawText(
        text: Component,
        context: WorldRenderContext,
        pos: Vec3,
        depthTest: Boolean = true,
        scale: Float = 1f
    ) {
        val screen = worldToScreen(pos) ?: return
        pendingLabels.add(PendingLabel(text, null, screen.first, screen.second, -1, (0xAA shl 24)))
    }

    /**
     * Queues a waypoint label (name + distance) to be rendered in the HUD pass.
     */
    fun renderWaypoint(
        text: Component,
        context: WorldRenderContext,
        pos: Vec3,
        textColor: Int = -1,
        backgroundColor: Int = (0xAA shl 24),
    ) {
        val player = mc.player ?: return
        val d = pos.distanceTo(player.position())
        val distText = Component.literal("${d.toInt()}m").withStyle(ChatFormatting.YELLOW)
        val screen = worldToScreen(pos) ?: return
        pendingLabels.add(PendingLabel(text, distText, screen.first, screen.second, textColor, backgroundColor))
    }

    /**
     * Draws a filled box at a given position
     */
    fun drawBox(
        context: WorldRenderContext,
        x: Double,
        y: Double,
        z: Double,
        width: Double,
        height: Double,
        depth: Double,
        color: Color,
        depthTest: Boolean
    ) {
        val layer = if (depthTest) {
            DulkirRenderTypes.DULKIR_TRIANGLE_STRIP
        } else {
            DulkirRenderTypes.DULKIR_TRIANGLE_STRIP_ESP
        }

        val matrices = context.matrices() ?: return
        val buf = RenderUtil.getBufferFor(layer)
        matrices.pushPose()
        val camera = context.gameRenderer().mainCamera
        val camPos = camera.position()
        matrices.translate(x - camPos.x, y - camPos.y, z - camPos.z)
        addBoxVertices(matrices.last(), buf, 0.0, 0.0, 0.0, width, height, depth, color.rgb)
        layer.draw(buf.buildOrThrow())
        matrices.popPose()
    }
}
