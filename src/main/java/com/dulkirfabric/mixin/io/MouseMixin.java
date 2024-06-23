package com.dulkirfabric.mixin.io;

import com.dulkirfabric.features.InventoryScale;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mouse.class)
public class MouseMixin {

    @ModifyExpressionValue(
            method = "onMouseButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/Window;getScaledWidth()I"
            )
    )
    public int onMouseButtonWidth(int originalScaledWidth) {
        return (int) (originalScaledWidth / InventoryScale.INSTANCE.getScale());
    }

    @ModifyExpressionValue(
            method = "onMouseButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/Window;getScaledHeight()I"
            )
    )
    public int onMouseButtonHeight(int originalScaledHeight) {
        return (int) (originalScaledHeight / InventoryScale.INSTANCE.getScale());
    }

    @ModifyExpressionValue(
            method = "onCursorPos",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/Window;getScaledWidth()I"
            )
    )
    public int onCursorPosWidth(int originalScaledWidth) {
        return (int) (originalScaledWidth / InventoryScale.INSTANCE.getScale());
    }

    @ModifyExpressionValue(
            method = "onCursorPos",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/Window;getScaledHeight()I"
            )
    )
    public int onCursorPosHeight(int originalScaledHeight) {
        return (int) (originalScaledHeight / InventoryScale.INSTANCE.getScale());
    }

    @ModifyExpressionValue(
            method = "onMouseScroll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/Window;getScaledWidth()I"
            )
    )
    public int onMouseScrollWidth(int originalScaledWidth) {
        return (int) (originalScaledWidth / InventoryScale.INSTANCE.getScale());
    }

    @ModifyExpressionValue(
            method = "onMouseScroll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/Window;getScaledHeight()I"
            )
    )
    public int onMouseScrollHeight(int originalScaledHeight) {
        return (int) (originalScaledHeight / InventoryScale.INSTANCE.getScale());
    }

}

