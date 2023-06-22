package com.dulkirfabric.mixin;

import com.dulkirfabric.events.PlaySoundEvent;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {

    @ModifyExpressionValue(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundInstance;canPlay()Z"))
    public boolean onSound(boolean original, SoundInstance sound) {
        return !(new PlaySoundEvent(sound).post());
    }
}
