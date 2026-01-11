package com.dulkirfabric.mixin.render;

import com.dulkirfabric.features.InventoryScale;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractContainerScreen.class)
public class HandledScreenMixin {

    // To fix item render pos
    @WrapMethod(
            method = "render"
    )
    private void dulkir$modifyMouse(GuiGraphics graphics, int mouseX, int mouseY, float deltaTicks,
                                    Operation<Void> original) {
        original.call(
                graphics,
                (int) Math.floor(mouseX * InventoryScale.INSTANCE.getScale()),
                (int) Math.floor(mouseY * InventoryScale.INSTANCE.getScale()),
                deltaTicks
        );
    }

    // To fix tooltip render pos
    @WrapMethod(
            method = "renderTooltip"
    )
    private void dulkir$modifyTooltipPos(GuiGraphics graphics, int x, int y, Operation<Void> original) {
        original.call(
                graphics,
                (int) Math.floor(x * InventoryScale.INSTANCE.getScale()),
                (int) Math.floor(y * InventoryScale.INSTANCE.getScale())
        );
    }

}
