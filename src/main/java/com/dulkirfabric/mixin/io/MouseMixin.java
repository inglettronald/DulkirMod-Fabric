package com.dulkirfabric.mixin.io;

import com.dulkirfabric.features.InventoryScale;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mouse.class)
public class MouseMixin {

    @ModifyExpressionValue(
            method = "scaleX",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/Window;getScaledWidth()I"
            )
    )
    private static int modifyWidth(int originalScaledWidth) {
        return (int) (originalScaledWidth / InventoryScale.INSTANCE.getScale());
    }

    @ModifyExpressionValue(
            method = "scaleY",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/Window;getScaledHeight()I"
            )
    )
    private static int modifyHeight(int originalScaledHeight) {
        return (int) (originalScaledHeight / InventoryScale.INSTANCE.getScale());
    }

}

