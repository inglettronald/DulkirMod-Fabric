package com.dulkirfabric.mixin.render;

import com.dulkirfabric.config.DulkirConfig;
import com.dulkirfabric.features.InventoryScale;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {

    // No longer needed as of 1.21.4
    /*@ModifyArgs(
            method = "drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIIIFFFLnet/minecraft/entity/LivingEntity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;enableScissor(IIII)V"
            )
    )
    private static void modifyScissor(Args args) {
        for (int i = 0; i < 4; i++) {
            args.set(i, (int) (((int) args.get(i)) * InventoryScale.INSTANCE.getScale()));
        }
    }*/

}
