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

import com.dulkirfabric.config.DulkirConfig;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(PauseScreen.class)
public abstract class PauseScreenMixin extends ScreenMixin {

    @Shadow
    protected abstract Button openScreenButton(Component component, Supplier<Screen> supplier);

    @Unique
	private final Component dulkir$buttonText = Component.literal("Dulkir")
            .withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW);

	/**
	 * Method to create the config entry point button inside the escape menu
	 */
	@Inject(
			method = "createPauseMenu",
			at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screens/PauseScreen;OPTIONS:Lnet/minecraft/network/chat/Component;",
                    opcode = Opcodes.GETSTATIC
            )
	)
	private void initWidget(CallbackInfo ci, @Local GridLayout.RowHelper adder) {
		if (DulkirConfig.ConfigVars.getConfigOptions().getShowPauseMenuButton()) {
			adder.addChild(this.openScreenButton(dulkir$buttonText, new DulkirConfig()::getScreen));
		}
	}

}