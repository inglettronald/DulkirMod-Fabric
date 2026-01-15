package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import com.dulkirfabric.util.render.GlowingEntityInterface;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
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
    public float attackAnim;
    @Unique
    private int dulkir$animationTicks;
    @Unique
    private boolean dulkir$shouldGlow;
    @Unique
    private Color dulkir$glowColor;

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
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

    /**
     * Using a wrapMethod because another mod tries to cancel this
     */
    @WrapMethod(
            method = "updateSwingTime"
    )
    private void dulkir$modifySwingPos(Operation<Void> original) {
        original.call();
        int swingDuration = DulkirConfig.ConfigVars.getConfigOptions().getAnimationPreset().getSwingDuration();
        if (swingDuration == 6) {
            return;
        }
        if (dulkir$animationTicks > swingDuration) {
            dulkir$animationTicks = 0;
        }
        if (dulkir$animationTicks == 0) {
            this.attackAnim = 1F;
        } else {
            this.attackAnim = (dulkir$animationTicks - 1F) / swingDuration;
            dulkir$animationTicks++;
        }
    }

    @Inject(
            method = "swing(Lnet/minecraft/world/InteractionHand;)V",
            at = @At("HEAD")
    )
    public void dulkir$onSwing(InteractionHand interactionHand, CallbackInfo ci) {
        if (dulkir$animationTicks == 0) {
            dulkir$animationTicks = 1;
        }
    }

}