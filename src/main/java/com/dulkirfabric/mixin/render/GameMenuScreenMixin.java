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
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends ScreenMixin {

	private final Text buttonText = MutableText.of(new PlainTextContent.Literal("Dulkir"))
			.formatted(Formatting.BOLD, Formatting.YELLOW);

	@Shadow
	protected abstract ButtonWidget createButton(Text text, Supplier<Screen> screenSupplier);

	/**
	 * Method to create the config entry point button inside the escape menu
	 */
	@Inject(method = "initWidgets", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/GameMenuScreen;OPTIONS_TEXT:Lnet/minecraft/text/Text;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void initWidget(CallbackInfo ci, GridWidget gridWidget, GridWidget.Adder adder) {
		adder.add(this.createButton(buttonText, new DulkirConfig()::getScreen));
	}

}