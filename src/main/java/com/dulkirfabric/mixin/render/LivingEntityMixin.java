package com.dulkirfabric.mixin.render;

import com.dulkirfabric.util.GlowingEntityInterface;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.awt.*;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements GlowingEntityInterface {

    @Unique
    private boolean shouldGlow_DulkirMod;
    @Unique
    private Color glowColor_DulkirMod;
    @Unique
    private boolean shouldESP_DulkirMod;

    @Override
    public void setDulkirEntityGlow(boolean shouldGlow, @NotNull Color glowColor, boolean shouldESP) {
        this.shouldGlow_DulkirMod = shouldGlow;
        this.glowColor_DulkirMod = glowColor;
        this.shouldESP_DulkirMod = shouldESP;
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

    @Override
    public boolean shouldDulkirEntityESP() {
        return shouldESP_DulkirMod;
    }
}
