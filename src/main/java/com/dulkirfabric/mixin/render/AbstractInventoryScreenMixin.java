package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractInventoryScreen.class)
public class AbstractInventoryScreenMixin {

    @Inject(
            method = "drawStatusEffects",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onDrawStatusEffects(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getStatusEffectHidden()) {
            ci.cancel();
        }
    }
}
