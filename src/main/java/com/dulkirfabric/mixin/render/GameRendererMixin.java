package com.dulkirfabric.mixin.render;


import com.dulkirfabric.features.InventoryScale;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GameRenderer.class, priority = 1001)
public class GameRendererMixin {

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;renderWithTooltipAndSubtitles" +
                            "(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"
            )
    )
    private void dulkir$wrapScreenRender(Screen instance, GuiGraphics guiGraphics, int mouseX, int mouseY, float f,
                                         Operation<Void> original) {
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().scale(InventoryScale.INSTANCE.getScale(), InventoryScale.INSTANCE.getScale());
        original.call(
                instance,
                guiGraphics,
                mouseX,
                mouseY,
                f
        );
        guiGraphics.pose().popMatrix();
    }

}