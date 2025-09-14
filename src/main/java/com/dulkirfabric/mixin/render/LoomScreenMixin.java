package com.dulkirfabric.mixin.render;

import com.dulkirfabric.features.InventoryScale;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoomScreen.class)
public class LoomScreenMixin {

    @Inject(
            method = "drawBanner",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Matrix3x2fStack;pushMatrix()Lorg/joml/Matrix3x2fStack;",
                    shift = At.Shift.AFTER
            )
    )
    public void onCreateMatrix(DrawContext context, int x, int y, Sprite sprite, CallbackInfo ci) {
        context.getMatrices().scale(InventoryScale.INSTANCE.getScale(), InventoryScale.INSTANCE.getScale());
    }

}

