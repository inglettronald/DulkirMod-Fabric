package com.dulkirfabric.mixin.render;

import com.dulkirfabric.features.InventoryScale;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoomScreen.class)
public class LoomScreenMixin {

    @Inject(
            method = "renderBannerOnButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Matrix3x2fStack;pushMatrix()Lorg/joml/Matrix3x2fStack;",
                    shift = At.Shift.AFTER
            )
    )
    public void onCreateMatrix(GuiGraphics context, int x, int y, TextureAtlasSprite sprite, CallbackInfo ci) {
        context.pose().scale(InventoryScale.INSTANCE.getScale(), InventoryScale.INSTANCE.getScale());
    }

}

