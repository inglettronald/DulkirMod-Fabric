package com.dulkirfabric.mixin;

import com.dulkirfabric.util.render.ItemChangeHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();

    @Inject(
            method = "getTooltip",
            at = @At("HEAD")
    )
    private void onGetTooltip(Item.TooltipContext context, PlayerEntity player,
                              TooltipType type, CallbackInfoReturnable<List<Text>> cir) {
        ItemChangeHandler.INSTANCE.handle(this.getItem().getName().getString());
    }

}
