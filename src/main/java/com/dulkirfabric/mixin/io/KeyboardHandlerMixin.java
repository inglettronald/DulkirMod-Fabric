package com.dulkirfabric.mixin.io;

import com.dulkirfabric.events.WorldKeyPressEvent;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @Inject(
            method = "keyPress",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/KeyMapping;click(Lcom/mojang/blaze3d/platform/InputConstants$Key;)V"
            )
    )
    public void onKeyBoardInWorld(long l, int i, KeyEvent keyEvent, CallbackInfo ci) {
        new WorldKeyPressEvent(keyEvent.key(), keyEvent.scancode(), keyEvent.modifiers()).post();
    }

}
