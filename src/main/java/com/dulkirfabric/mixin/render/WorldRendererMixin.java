package com.dulkirfabric.mixin.render;

import com.dulkirfabric.util.GlowingEntityInterface;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.dulkirfabric.DulkirModFabric.mc;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow
    private @Nullable Framebuffer entityOutlinesFramebuffer;
    @Shadow
    @Final
    public BufferBuilderStorage bufferBuilders;
    @Shadow @Final private MinecraftClient client;
    @Unique
    boolean shouldRenderOutlinesDirect = false;

    @Unique
    Framebuffer defaultEntityOutlineBuffer;

    @ModifyExpressionValue(method = "render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getTeamColorValue()I"))
    public int getGlowColor(int existing, @Local Entity entity) {
        if (entity instanceof GlowingEntityInterface dEntity) {
            if (dEntity.getDulkirEntityGlowColor() != null) {
                return dEntity.getDulkirEntityGlowColor().getRGB();
            }
        }
        return existing;
    }

    @Inject(method = "onResized", at = @At("HEAD"))
    public void onResized(int width, int height, CallbackInfo ci) {
        if (defaultEntityOutlineBuffer == null) {
            defaultEntityOutlineBuffer = new SimpleFramebuffer(width, height, true, false);
            defaultEntityOutlineBuffer.setClearColor(0, 0, 0, 0);
        } else {
            defaultEntityOutlineBuffer.resize(width, height, false);
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilderStorage;getEntityVertexConsumers()Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;", shift = At.Shift.AFTER))
    public void saveDefaultBufferESP(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
        if (defaultEntityOutlineBuffer != null) {
            defaultEntityOutlineBuffer.copyDepthFrom(this.entityOutlinesFramebuffer);
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;shouldRender(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/Frustum;DDD)Z"))
    public void setOutlineESP(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci, @Local Entity entity) {
        if (entity instanceof GlowingEntityInterface dEntity && dEntity.shouldDulkirEntityGlow() && !dEntity.shouldDulkirEntityESP()) {
            if (this.entityOutlinesFramebuffer != null)
                this.entityOutlinesFramebuffer.copyDepthFrom(mc.getFramebuffer());
        } else {
            // give esp back here
            if (this.entityOutlinesFramebuffer != null)
                this.entityOutlinesFramebuffer.copyDepthFrom(defaultEntityOutlineBuffer);
        }
    }
}
