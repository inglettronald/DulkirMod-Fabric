package com.dulkirfabric.mixin.render;

import com.dulkirfabric.events.InventoryKeyPressEvent;
import com.dulkirfabric.events.SlotRenderEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Inject(method = "keyPressed", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;handleHotbarKeyPressed(II)Z",
            shift = At.Shift.BEFORE), cancellable = true)
    public void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (new InventoryKeyPressEvent(keyCode, scanCode, modifiers).post()) {
            cir.setReturnValue(true);
        }
    }

    /**@Inject(method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V",
            at = @At("HEAD"), cancellable = true)
    public void onMouseClickedSlot(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        if (IsSlotProtectedEvent.shouldBlockInteraction(slot)) {
            ci.cancel();
        }
    }*/

    @Inject(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlot(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/screen/slot/Slot;)V",
            shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onAfterDrawSlot(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci, int i, int j, int k, Slot slot) {
        SlotRenderEvent.After event = new SlotRenderEvent.After(context, slot, mouseX, mouseY, delta);
        event.post();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlot(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/screen/slot/Slot;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onBeforeDrawSlot(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci, int i, int j, int k, Slot slot) {
        SlotRenderEvent.Before event = new SlotRenderEvent.Before(context, slot, mouseX, mouseY, delta);
        event.post();
    }
}
