package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import com.dulkirfabric.util.Utils;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(
            method = "renderEffects",
            at = @At("HEAD"),
            cancellable = true
    )
    private void dulkir$renderStatusEffectOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getStatusEffectHidden()) {
            ci.cancel();
        }
    }

    @WrapWithCondition(
            method = "displayScoreboardSidebar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)V",
                    ordinal = 2
            )
    )
    public boolean removeScoreBoardNumbers(GuiGraphics instance, Font font, Component component, int i,
                                           int j, int k, boolean bl) {
        return !(DulkirConfig.ConfigVars.getConfigOptions().getHideScoreboardNumbers());
    }

    @Inject(
            method = "renderArmor",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onGrabArmorAmount(GuiGraphics guiGraphics, Player player, int i, int j, int k, int l, CallbackInfo ci) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getHideArmorOverlay() && Utils.INSTANCE.isInSkyblock()) {
            ci.cancel();
        }
    }

    @Inject(
            method = "renderFood",
            at = @At("HEAD"),
            cancellable = true
    )
    public void dulkir$renderFood(GuiGraphics guiGraphics, Player player, int i, int j, CallbackInfo ci) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getHideHungerOverlay() && Utils.INSTANCE.isInSkyblock()) {
            ci.cancel();
        }
    }

    @Inject(
            method = "renderSelectedItemName",
            at = @At("HEAD"),
            cancellable = true
    )
    public void changeItemDisplay(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getHideHeldItemTooltip()) {
            ci.cancel();
        }
    }

}
