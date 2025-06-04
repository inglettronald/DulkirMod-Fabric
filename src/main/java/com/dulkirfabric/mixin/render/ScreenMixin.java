/**
 *   This Source Code Form is subject to the terms of the Mozilla Public
 *   License, v. 2.0. If a copy of the MPL was not distributed with this
 *   file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * If it is not possible or desirable to put the notice in a particular
 * file, then You may include the notice in a location (such as a LICENSE
 * file in a relevant directory) where a recipient would be likely to look
 * for such a notice.
 *
 * You may add additional accurate notices of copyright ownership.
 */

package com.dulkirfabric.mixin.render;

import com.dulkirfabric.features.InventoryScale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static java.lang.Math.ceil;


@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Shadow public int width;

    @Shadow public int height;

    @Inject(
            method = "init(Lnet/minecraft/client/MinecraftClient;II)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screen/Screen;height:I",
                    shift = At.Shift.AFTER
            )
    )
    public void onInitAfterViewportSizeSet(MinecraftClient client, int width, int height, CallbackInfo ci) {
        this.width = (int) ceil(width / InventoryScale.INSTANCE.getScale());
        this.height = (int) ceil(height /  InventoryScale.INSTANCE.getScale());
    }

    @Inject(
            method = "resize",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screen/Screen;height:I",
                    shift = At.Shift.AFTER
            )
    )
    public void onResizeAfterViewportSizeSet(MinecraftClient client, int width, int height, CallbackInfo ci) {
        this.width = (int) ceil(width / InventoryScale.INSTANCE.getScale());
        this.height = (int) ceil(height / InventoryScale.INSTANCE.getScale());
    }

}