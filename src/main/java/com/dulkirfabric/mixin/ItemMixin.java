package com.dulkirfabric.mixin;

import com.dulkirfabric.features.CooldownDisplays;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "isItemBarVisible(Lnet/minecraft/item/ItemStack;)Z",
    at = @At("HEAD"), cancellable = true)
    public void shouldDisplayDurabilityBar(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        CooldownDisplays.INSTANCE.shouldDisplay(stack, cir);
    }
    @Inject(method = "getItemBarStep(Lnet/minecraft/item/ItemStack;)I",
    at = @At("HEAD"), cancellable = true)
    public void calculateItemHealth(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        CooldownDisplays.INSTANCE.calcDurability(stack, cir);
    }
}
