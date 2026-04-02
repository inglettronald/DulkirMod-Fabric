package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(EffectsInInventory.class)
public class EffectsInInventoryMixin {

    @Inject(
            method = "renderEffects",
            at = @At("HEAD"),
            cancellable = true
    )
    private void dulkir$cancelEffects(GuiGraphics guiGraphics, Collection<MobEffectInstance> effects, int i, int j,
                                      int k, int l, int m, CallbackInfo ci) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getStatusEffectHidden()) {
            ci.cancel();
        }
    }

}
