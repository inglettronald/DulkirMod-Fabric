package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import com.dulkirfabric.util.render.GlowingEntityInterface;
import net.minecraft.entity.*;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
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
public abstract class LivingEntityMixin extends Entity implements Attackable, GlowingEntityInterface {

    @Shadow
    public float handSwingProgress;

    @Unique
    private int dulkir$animationTicks;
    @Unique
    private boolean dulkir$shouldGlow;
    @Unique
    private Color dulkir$glowColor;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void dulkir$setEntityGlow(boolean shouldGlow, @NotNull Color glowColor) {
        this.dulkir$shouldGlow = shouldGlow;
        this.dulkir$glowColor = glowColor;
    }

    @Override
    public boolean dulkir$shouldEntityGlow() {
        return dulkir$shouldGlow;
    }

    @Nullable
    @Override
    public Color dulkir$getGlowColor() {
        return dulkir$glowColor;
    }

    @Inject(
            method = "tickHandSwing",
            at = @At(
                    value = "TAIL"
            )
    )
    private void dulkir$modifySwingPos(CallbackInfo ci) {
        int swingDuration = DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getSwingDuration();
        if (swingDuration == 6) {
            return;
        }
        if (dulkir$animationTicks > swingDuration) {
            dulkir$animationTicks = 0;
        }
        if (dulkir$animationTicks == 0) {
            handSwingProgress = 1F;
        } else {
            handSwingProgress = (dulkir$animationTicks - 1F) / swingDuration;
            dulkir$animationTicks++;
        }
    }

    @Inject(
            method = "swingHand(Lnet/minecraft/util/Hand;Z)V",
            at = @At("HEAD")
    )
    public void dulkir$onSwing(Hand hand, boolean fromServerPlayer, CallbackInfo ci) {
        if (dulkir$animationTicks == 0) {
            dulkir$animationTicks = 1;
        }
    }
}