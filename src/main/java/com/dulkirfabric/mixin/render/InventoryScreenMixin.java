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
import org.spongepowered.asm.mixin.injection.Inject;

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
        float scale = InventoryScale.INSTANCE.getScale();
        if (DulkirModFabric.mc.screen instanceof CreativeModeInventoryScreen) {
            newX = mouseX / scale;
            newY = mouseY / scale;
        } else {
            newX = mouseX * scale;
            newY = mouseY * scale;
        }
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().scale(1 / scale, 1 / scale);
        original.call(
                guiGraphics,
                (int) (x1 * scale), (int) (y1 * scale),
                (int) (x2 * scale), (int) (y2 * scale),
                (int) (size * scale),
                f,
                newX,
                newY,
                entity
        );
        guiGraphics.pose().popMatrix();
    }

}
