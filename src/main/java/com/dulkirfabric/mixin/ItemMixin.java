package com.dulkirfabric.mixin;

import com.dulkirfabric.config.DulkirConfig;
import com.dulkirfabric.features.CooldownDisplays;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(
            method = "isBarVisible",
            at = @At("HEAD"),
            cancellable = true
    )
    public void shouldDisplayDurabilityBar(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getDuraCooldown()) {
            CooldownDisplays.INSTANCE.shouldDisplay(itemStack, cir);
        }
    }

    @Inject(
            method = "getBarWidth",
            at = @At("HEAD"),
            cancellable = true
    )
    public void calculateItemHealth(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (DulkirConfig.ConfigVars.getConfigOptions().getDuraCooldown()) {
            CooldownDisplays.INSTANCE.calcDurability(stack, cir);
        }
    }

}
