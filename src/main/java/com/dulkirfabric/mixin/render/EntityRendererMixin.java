package com.dulkirfabric.mixin.render;

import com.dulkirfabric.util.render.GlowingEntityInterface;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @ModifyExpressionValue(
            method = "extractRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getTeamColor()I"
            )
    )
    public int getGlowColor(int existing, @Local(argsOnly = true) Entity entity) {
        if (entity instanceof GlowingEntityInterface dEntity) {
            if (dEntity.dulkir$getGlowColor() != null) {
                return dEntity.dulkir$getGlowColor().getRGB();
            }
        }
        return existing;
    }

}
