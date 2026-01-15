package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import com.dulkirfabric.features.InventoryScale;
import com.dulkirfabric.features.TooltipImpl;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3x2fStack;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {

    @Shadow
    @Final
    private Matrix3x2fStack pose;

    @WrapOperation(
            method = "renderTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;" +
                            "positionTooltip(IIIIII)Lorg/joml/Vector2ic;"
            )
    )
    public Vector2ic drawTooltip(ClientTooltipPositioner positionerInstance, int sw, int sh, int mx, int my, int tw,
                                 int th, Operation<Vector2ic> operation) {
        if (!DulkirConfig.ConfigVars.getConfigOptions().getToolTipFeatures()) {
            return operation.call(positionerInstance, sw, sh, mx, my, tw, th);
        }
        net.minecraft.client.gui.screens.Screen screen = Minecraft.getInstance().screen;
        if (!(screen instanceof AbstractContainerScreen<?>)) {
            return operation.call(positionerInstance, sw, sh, mx, my, tw, th);
        }
        float scale = InventoryScale.INSTANCE.getScale();
        float tooltipScale = DulkirConfig.ConfigVars.getConfigOptions().getTooltipScale();
        Vector2ic v = operation.call(
                positionerInstance,
                (int) (sw / scale),
                (int) (sh / scale),
                mx ,
                my,
                (int) (tw * tooltipScale),
                (int) (th * tooltipScale)
        );
        return TooltipImpl.INSTANCE.calculatePos(
                v,
                (int) (tw * tooltipScale),
                (int) (th * tooltipScale),
                (int) (sw / scale),
                (int) (sh / scale)
        );
    }

    @Inject(
            method = "renderTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Matrix3x2fStack;pushMatrix()Lorg/joml/Matrix3x2fStack;",
                    shift = At.Shift.AFTER
            )
    )
    public void onPush(Font font, List<ClientTooltipComponent> list, int i, int j,
                       ClientTooltipPositioner clientTooltipPositioner, ResourceLocation resourceLocation, CallbackInfo ci) {
        if (!DulkirConfig.ConfigVars.getConfigOptions().getToolTipFeatures()) {
            return;
        }
        if (Minecraft.getInstance().screen instanceof AbstractContainerScreen<?>) {
            TooltipImpl.INSTANCE.applyScale(this.pose);
        }
    }
}
