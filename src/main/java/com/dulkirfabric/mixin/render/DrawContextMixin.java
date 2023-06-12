package com.dulkirfabric.mixin.render;

import com.dulkirfabric.features.TooltipImpl;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DrawContext.class)
public class DrawContextMixin {

    @WrapOperation(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V",
            at = @At(target = "Lnet/minecraft/client/gui/tooltip/TooltipPositioner;getPosition(IIIIII)Lorg/joml/Vector2ic;", value = "INVOKE"))
    public Vector2ic drawTooltip(TooltipPositioner positionerInstance, int sw, int sh, int mx, int my, int tw, int th, Operation<Vector2ic> operation) {
        Vector2ic v = operation.call(positionerInstance, sw, sh, mx, my, tw, th);
        return TooltipImpl.INSTANCE.calculatePos(v);
    }
}
