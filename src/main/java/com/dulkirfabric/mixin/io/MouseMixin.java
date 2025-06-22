package com.dulkirfabric.mixin.io;

import com.dulkirfabric.events.MousePressEvent;
import com.dulkirfabric.features.InventoryScale;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
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
    private static int dulkir$modifyWidth(int originalScaledWidth) {
        return (int) (originalScaledWidth / InventoryScale.INSTANCE.getScale());
    }

    @ModifyExpressionValue(
            method = "scaleY",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/Window;getScaledHeight()I"
            )
    )
    private static int dulkir$modifyHeight(int originalScaledHeight) {
        return (int) (originalScaledHeight / InventoryScale.INSTANCE.getScale());
    }

    @WrapMethod(
            method = "onMouseButton"
    )
    private void dulkir$onMouseButton(long window, int button, int action, int mods, Operation<Void> original) {
        MousePressEvent event = new MousePressEvent(button);
        event.post();
        if (!event.isCancelled()) {
            original.call(window, button, action, mods);
        }
    }

}

