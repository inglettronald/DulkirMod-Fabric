package com.dulkirfabric.mixin.render;

import com.dulkirfabric.events.TooltipRenderChangeEvent;
import com.dulkirfabric.features.TooltipImpl;
import com.dulkirfabric.util.ItemChangeHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DrawContext.class)
public class DrawContextMixin {
    @Shadow @Final private MatrixStack matrices;
    List<TooltipComponent> prevComponents;

    @WrapOperation(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V",
            at = @At(target = "Lnet/minecraft/client/gui/tooltip/TooltipPositioner;getPosition(IIIIII)Lorg/joml/Vector2ic;", value = "INVOKE"))
    public Vector2ic drawTooltip(TooltipPositioner positionerInstance, int sw, int sh, int mx, int my, int tw, int th, Operation<Vector2ic> operation) {
        Vector2ic v = operation.call(positionerInstance, sw, sh, mx, my, tw, th);
        return TooltipImpl.INSTANCE.calculatePos(v);
    }

    @Inject(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V",
            at = @At(target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", value = "INVOKE"))
    public void onPush(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, CallbackInfo ci) {
        TooltipImpl.INSTANCE.applyScale(matrices);
    }
}
