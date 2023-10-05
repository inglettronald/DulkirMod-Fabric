package com.dulkirfabric.mixin.render;


import com.dulkirfabric.features.InventoryScale;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = GameRenderer.class, priority = 1001)
public class GameRendererMixin {

    @ModifyArg(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/Screen;renderWithTooltip(Lnet/minecraft/client/gui/DrawContext;IIF)V"),
            index = 1)
    public int fixMouseX(int mouseX) {
        return (int) (mouseX / InventoryScale.INSTANCE.getScale());
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/Screen;renderWithTooltip(Lnet/minecraft/client/gui/DrawContext;IIF)V"),
            index = 2)
    public int fixMouseY(int mouseY) {
        return (int) (mouseY / InventoryScale.INSTANCE.getScale());
    }


    @Inject(method = "render", at = @At(value = "FIELD",
            opcode = Opcodes.GETFIELD,
            target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;",
            shift = At.Shift.BEFORE,
            ordinal = 1))
    public void onScreenRenderPre(float tickDelta, long startTime, boolean tick, CallbackInfo ci, @Local DrawContext drawContext) {
        drawContext.getMatrices().push();
        drawContext.getMatrices().scale(InventoryScale.INSTANCE.getScale(), InventoryScale.INSTANCE.getScale(), 1f);
    }


    @Inject(method = "render", at = @At(value = "FIELD",
            opcode = Opcodes.GETFIELD,
            target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;",
            shift = At.Shift.AFTER,
            ordinal = 3)
    )
    public void onScreenRenderPost(float tickDelta, long startTime, boolean tick, CallbackInfo ci, @Local DrawContext drawContext) {
        drawContext.getMatrices().pop();
    }
}