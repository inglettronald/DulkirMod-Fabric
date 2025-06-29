package com.dulkirfabric.mixin.render;

import com.dulkirfabric.events.InventoryKeyPressEvent;
import com.dulkirfabric.features.InventoryScale;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {

    @Inject(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;handleHotbarKeyPressed(II)Z",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    public void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (new InventoryKeyPressEvent(keyCode, scanCode, modifiers).post()) {
            cir.setReturnValue(true);
        }
    }

    // To fix item render pos
    @WrapMethod(
            method = "render"
    )
    private void dulkir$modifyMouse(DrawContext context, int mouseX, int mouseY, float deltaTicks, Operation<Void> original) {
        original.call(
                context,
                (int) Math.floor(mouseX * InventoryScale.INSTANCE.getScale()),
                (int) Math.floor(mouseY * InventoryScale.INSTANCE.getScale()),
                deltaTicks
        );
    }

    // To fix tooltip render pos
    @WrapMethod(
            method = "drawMouseoverTooltip"
    )
    private void dulkir$modifyTooltipPos(DrawContext drawContext, int x, int y, Operation<Void> original) {
        original.call(
                drawContext,
                (int) Math.floor(x * InventoryScale.INSTANCE.getScale()),
                (int) Math.floor(y * InventoryScale.INSTANCE.getScale())
        );
    }

}
