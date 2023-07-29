package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void onFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getHideFireOverlay())
            ci.cancel();
    }
}
