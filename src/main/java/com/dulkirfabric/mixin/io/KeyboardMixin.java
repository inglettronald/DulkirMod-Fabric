package com.dulkirfabric.mixin.io;

import com.dulkirfabric.events.WorldKeyPressEvent;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(
            method = "onKey",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/option/KeyBinding;onKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;)V"
            )
    )
    public void onKeyBoardInWorld(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        new WorldKeyPressEvent(key, scancode, modifiers).post();
    }
}
