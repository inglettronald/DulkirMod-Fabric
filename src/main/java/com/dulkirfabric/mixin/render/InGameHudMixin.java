package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import com.dulkirfabric.util.Utils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(
            method = "renderStatusEffectOverlay",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onRenderStatusEffectOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getStatusEffectHidden()) {
            ci.cancel();
        }
    }

    @WrapWithCondition(
            method = "method_55440",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;" +
                            "Lnet/minecraft/text/Text;IIIZ)I",
                    ordinal = 2
            )
    )
    public boolean removeScoreBoardNumbers(DrawContext instance, TextRenderer textRenderer, Text text,
                                       int x, int y, int color, boolean shadow) {
        return !(DulkirConfig.ConfigVars.getConfigOptions().getHideScoreboardNumbers());
    }

    @WrapWithCondition(
            method = "renderStatusBars(Lnet/minecraft/client/gui/DrawContext;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderArmor(Lnet/minecraft/client/gui/" +
                            "DrawContext;Lnet/minecraft/entity/player/PlayerEntity;IIII)V"
            )
    )
    public boolean onGrabArmorAmount(DrawContext context, PlayerEntity player, int i, int j, int k, int x) {
        return !(DulkirConfig.ConfigVars.getConfigOptions().getHideArmorOverlay() && Utils.INSTANCE.isInSkyblock());
    }

    @ModifyExpressionValue(
            method = "renderStatusBars(Lnet/minecraft/client/gui/DrawContext;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;getHeartCount(Lnet/minecraft/entity/LivingEntity;)I"
            )
    )
    public int onCheckForRiding(int original) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getHideHungerOverlay() && Utils.INSTANCE.isInSkyblock())
            return 1;
        return original;
    }

    @Inject(
            method = "renderHeldItemTooltip",
            at = @At("HEAD"),
            cancellable = true
    )
    public void changeItemDisplay (DrawContext context, CallbackInfo ci) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getHideHeldItemTooltip())
            ci.cancel();
    }
}
