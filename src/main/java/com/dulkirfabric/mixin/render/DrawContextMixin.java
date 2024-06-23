package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import com.dulkirfabric.features.InventoryScale;
import com.dulkirfabric.features.TooltipImpl;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
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

    @WrapOperation(
            method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/" +
                    "client/gui/tooltip/TooltipPositioner;)V",
            at = @At(
                    target = "Lnet/minecraft/client/gui/tooltip/TooltipPositioner;getPosition(IIIIII)Lorg/joml/Vector2ic;",
                    value = "INVOKE"
            )
    )
    public Vector2ic drawTooltip(TooltipPositioner positionerInstance, int sw, int sh, int mx, int my, int tw, int th, Operation<Vector2ic> operation) {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (!(screen instanceof HandledScreen)) {
            return operation.call(positionerInstance, sw, sh, mx, my, tw, th);
        }
        float scale = InventoryScale.INSTANCE.getScale();
        float tooltipScale = DulkirConfig.ConfigVars.getConfigOptions().getTooltipScale();
        Vector2ic v = operation.call(positionerInstance, (int) (sw / scale), (int) (sh / scale),
                mx , my, (int) (tw * tooltipScale), (int) (th * tooltipScale));
        return TooltipImpl.INSTANCE.calculatePos(v, (int) (tw * tooltipScale),
                (int) (th * tooltipScale), (int) (sw / scale), (int) (sh / scale));
    }

    @Inject(
            method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/" +
                    "minecraft/client/gui/tooltip/TooltipPositioner;)V",
            at = @At(
                    target = "Lnet/minecraft/client/util/math/MatrixStack;push()V",
                    value = "INVOKE",
                    shift = At.Shift.AFTER
            )
    )
    public void onPush(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen instanceof HandledScreen) {
            TooltipImpl.INSTANCE.applyScale(matrices);
        }
    }
}
