/*
 * This file is part of the SCTMasaExtra project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  late_maple and contributors
 *
 * SCTMasaExtra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SCTMasaExtra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with SCTMasaExtra.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.sctserver.sctmasaextra.config;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.data.ModInfo;
import top.sctserver.sctmasaextra.SCTMasaExtraMod;
import top.sctserver.sctmasaextra.gui.SCTMasaExtraConfigGui;

public class MalilibInitializer
{
	public static void init()
	{
		InitializationHandler.getInstance().registerInitializationHandler(() ->
		{
			ConfigManager.getInstance().registerConfigHandler(SCTMasaExtraMod.MOD_ID, SCTMasaExtraConfigStorage.getInstance());
			InputEventHandler.getKeybindManager().registerKeybindProvider(new KeybindProvider());
			Registry.CONFIG_SCREEN.registerConfigScreenFactory(new ModInfo(SCTMasaExtraMod.MOD_ID, SCTMasaExtraMod.MOD_NAME, SCTMasaExtraConfigGui::new));
			SCTMasaExtraConfigs.initConfigs();
		});
	}
}
