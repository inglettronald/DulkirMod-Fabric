package com.dulkirfabric.mixin.io;

import com.dulkirfabric.events.PlaySoundEvent;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {

    @Inject(
            method = "play(Lnet/minecraft/client/sound/SoundInstance;)" +
                    "Lnet/minecraft/client/sound/SoundSystem$PlayResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/sound/SoundInstance;getSound()Lnet/minecraft/client/sound/Sound;"
            ),
            cancellable = true
    )
    public void onSound(SoundInstance sound, CallbackInfoReturnable<SoundSystem.PlayResult> cir) {
        if (new PlaySoundEvent(sound).post()) {
            cir.cancel();
        }
    }

}
