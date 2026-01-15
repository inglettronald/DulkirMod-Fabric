package com.dulkirfabric.mixin.render;

import com.dulkirfabric.events.AddParticleEvent;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {

    @Inject(
            at = @At("HEAD"),
            method = "add",
            cancellable = true
    )
    public void onAddParticle(Particle particle, CallbackInfo ci) {
        if (new AddParticleEvent(particle).post()) {
            ci.cancel();
        }
    }

}
