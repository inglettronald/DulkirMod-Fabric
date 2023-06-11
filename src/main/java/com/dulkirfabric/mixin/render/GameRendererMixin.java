package com.dulkirfabric.mixin.render;


import com.dulkirfabric.config.DulkirConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    private double normalScale = -1;

    @Inject(method = "render", at = @At(value = "HEAD"))
    public void onRenderStart(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (!DulkirConfig.ConfigVars.getConfigOptions().getInvScaleBool()) return;
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen instanceof HandledScreen<?>) {
            normalScale = MinecraftClient.getInstance().getWindow().getScaleFactor();
            MinecraftClient.getInstance().getWindow().setScaleFactor(DulkirConfig.ConfigVars.getConfigOptions().getInventoryScale());
        }
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    public void onRenderEnd(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (!DulkirConfig.ConfigVars.getConfigOptions().getInvScaleBool()) return;
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen instanceof HandledScreen<?>) {
            MinecraftClient.getInstance().getWindow().setScaleFactor(normalScale);
        }
    }

}