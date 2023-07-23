package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import com.dulkirfabric.util.render.GlowingEntityInterface;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements GlowingEntityInterface {

    @Shadow
    public float handSwingProgress;
    @Unique
    private int animationTicks;

    @Unique
    private boolean shouldGlow_DulkirMod;
    @Unique
    private Color glowColor_DulkirMod;
    @Unique
    private boolean shouldESP_DulkirMod;

    @Override
    public void setDulkirEntityGlow(boolean shouldGlow, @NotNull Color glowColor) {
        this.shouldGlow_DulkirMod = shouldGlow;
        this.glowColor_DulkirMod = glowColor;
    }

    @Override
    public boolean shouldDulkirEntityGlow() {
        return shouldGlow_DulkirMod;
    }

    @Nullable
    @Override
    public Color getDulkirEntityGlowColor() {
        return glowColor_DulkirMod;
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;tickNewAi()V",
    shift = At.Shift.AFTER))
    public void onWhatever(CallbackInfo ci) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getSwingDuration() == 6) return;
        if (animationTicks > DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getSwingDuration()) {
            animationTicks = 0;
        }
        if (animationTicks == 0) {
            handSwingProgress = 1F;
        } else {
            handSwingProgress = (animationTicks - 1F) /
                    DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getSwingDuration();
            animationTicks++;
        }
    }

    @Inject(method = "swingHand(Lnet/minecraft/util/Hand;Z)V", at = @At("HEAD"))
    public void onSwing(Hand hand, boolean fromServerPlayer, CallbackInfo ci) {
        if (animationTicks == 0) {
            animationTicks = 1;
        }
    }
}