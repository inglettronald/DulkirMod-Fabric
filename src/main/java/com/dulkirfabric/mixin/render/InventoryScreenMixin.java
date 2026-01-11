package com.dulkirfabric.mixin.render;

import com.dulkirfabric.DulkirModFabric;
import com.dulkirfabric.features.InventoryScale;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {

    /**
     * This injection fixes the look position for the entity in Inventory Screens. Please don't use this as an actual
     * guide for how to implement this yourself - this entire feature is tech debt and terrible injections.
     */
    @WrapMethod(
            method = "renderEntityInInventoryFollowsMouse"
    )
    private static void dulkir$drawEntity(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int size,
                                          float f, float mouseX, float mouseY, LivingEntity entity,
                                          Operation<Void> original) {
        float newX, newY;
        if (DulkirModFabric.mc.screen instanceof CreativeModeInventoryScreen) {
            newX = mouseX / InventoryScale.INSTANCE.getScale();
            newY = mouseY / InventoryScale.INSTANCE.getScale();
        } else {
            newX = mouseX * InventoryScale.INSTANCE.getScale();
            newY = mouseY * InventoryScale.INSTANCE.getScale();
        }
        original.call(
                guiGraphics, x1, y1, x2, y2, size, f,
                newX,
                newY,
                entity
        );
    }

}
