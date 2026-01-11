package com.dulkirfabric.mixin.render;

import com.dulkirfabric.util.render.GlowingEntityInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(
            method = "shouldEntityAppearGlowing",
            at = @At("HEAD"),
            cancellable = true
    )
    public void outlineCheck(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof GlowingEntityInterface gEntity) {
            if (gEntity.dulkir$shouldEntityGlow()) {
                cir.setReturnValue(true);
            }
        }
    }

}
