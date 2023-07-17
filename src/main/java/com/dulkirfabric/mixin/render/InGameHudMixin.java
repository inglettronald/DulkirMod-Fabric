package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import com.dulkirfabric.util.ScoreBoardUtils;
import com.dulkirfabric.util.Utils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    public void onRenderStatusEffectOverlay(DrawContext context, CallbackInfo ci) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getStatusEffectHidden()) {
            ci.cancel();
        }
    }

    @Redirect(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"))
    public int renderScoreBoardSidebar(DrawContext instance, TextRenderer textRenderer, String text, int x, int y, int color, boolean shadow) {
        return 0;
    }

    @ModifyExpressionValue(method = "renderStatusBars(Lnet/minecraft/client/gui/DrawContext;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getArmor()I"))
    public int onGrabArmorAmount(int original) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getHideArmorOverlay() && Utils.INSTANCE.isInSkyblock()) {
            return 0;
        }
        return original;
    }
}
