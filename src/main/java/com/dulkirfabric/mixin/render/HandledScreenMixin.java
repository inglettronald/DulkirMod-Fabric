package com.dulkirfabric.mixin.render;

import com.dulkirfabric.events.InventoryKeyPressEvent;
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

}
