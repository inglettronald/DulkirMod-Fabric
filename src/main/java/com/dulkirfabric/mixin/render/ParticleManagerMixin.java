package com.dulkirfabric.mixin.render;

import com.dulkirfabric.events.AddParticleEvent;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.resource.ResourceReloader;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin implements ResourceReloader {

    @Inject(
            at = @At("HEAD"),
            method = "addParticle(Lnet/minecraft/client/particle/Particle;)V",
            cancellable = true
    )
    public void onAddParticle(Particle particle, CallbackInfo ci) {
        if (new AddParticleEvent(particle).post()) {
            ci.cancel();
        }
    }
}
