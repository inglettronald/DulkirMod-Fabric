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

package com.dulkirfabric

import com.dulkirfabric.config.DulkirConfig
import com.llamalad7.mixinextras.MixinExtrasBootstrap
import meteordevelopment.orbit.EventBus
import net.fabricmc.api.ModInitializer
import net.minecraft.client.MinecraftClient
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles


object DulkirModFabric : ModInitializer {
    private val logger = LoggerFactory.getLogger("dulkirmod-fabric")
	@JvmField
	val EVENT_BUS = EventBus()
	@JvmField
	val mc: MinecraftClient = MinecraftClient.getInstance()

	override fun onInitialize() {
		logger.info("Initializing DulkirMod...")


		// Orbit stuff
		EVENT_BUS.registerLambdaFactory("com.dulkirfabric") { lookupInMethod, klass ->
			lookupInMethod.invoke(null, klass, MethodHandles.lookup()) as MethodHandles.Lookup
		}

		// Mixin Extras
		MixinExtrasBootstrap.init()

		Registrations.registerEventListeners()
		Registrations.registerCommands()
		Registrations.registerEvents()

		DulkirConfig.loadConfig()
		System.setProperty("java.awt.headless", "false");
	}

}