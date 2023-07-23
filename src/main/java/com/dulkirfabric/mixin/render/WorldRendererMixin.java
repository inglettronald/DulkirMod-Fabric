package com.dulkirfabric.mixin.render;

import com.dulkirfabric.util.render.GlowingEntityInterface;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

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

}
