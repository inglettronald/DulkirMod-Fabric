package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Inject(
            method = "renderFire",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onFireOverlay(PoseStack poseStack, MultiBufferSource multiBufferSource,
                                      TextureAtlasSprite textureAtlasSprite, CallbackInfo ci) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getHideFireOverlay()) {
            ci.cancel();
        }
    }

}
