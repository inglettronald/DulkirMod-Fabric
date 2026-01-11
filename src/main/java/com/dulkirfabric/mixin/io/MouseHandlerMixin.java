package com.dulkirfabric.mixin.io;

import com.dulkirfabric.events.MousePressEvent;
import com.dulkirfabric.features.InventoryScale;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @ModifyExpressionValue(
            method = "getScaledXPos(Lcom/mojang/blaze3d/platform/Window;D)D",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/Window;getGuiScaledWidth()I"
            )
    )
    private static int dulkir$modifyWidth(int originalScaledWidth) {
        return (int) (originalScaledWidth / InventoryScale.INSTANCE.getScale());
    }

    @ModifyExpressionValue(
            method = "getScaledYPos(Lcom/mojang/blaze3d/platform/Window;D)D",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/Window;getGuiScaledHeight()I"
            )
    )
    private static int dulkir$modifyHeight(int originalScaledHeight) {
        return (int) (originalScaledHeight / InventoryScale.INSTANCE.getScale());
    }

    @WrapMethod(
            method = "onButton"
    )
    private void dulkir$onMouseButton(long l, MouseButtonInfo mouseButtonInfo, int i, Operation<Void> original) {
        MousePressEvent event = new MousePressEvent(mouseButtonInfo.button());
        event.post();
        if (!event.isCancelled()) {
            original.call(l, mouseButtonInfo, i);
        }
    }

}

