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

import java.util.List;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import top.sctserver.sctmasaextra.SCTMasaExtraMod;

public class KeybindProvider implements IKeybindProvider
{
	private static final List<IHotkey> HOTKEYS = List.of(SCTMasaExtraConfigs.OPEN_CONFIG_GUI);

	@Override
	public void addKeysToMap(IKeybindManager manager)
	{
		HOTKEYS.forEach(hotkey -> manager.addKeybindToMap(hotkey.getKeybind()));
	}

	@Override
	public void addHotkeys(IKeybindManager manager)
	{
		manager.addHotkeysForCategory(SCTMasaExtraMod.MOD_NAME, "sctmasaextra.hotkeys.category.main", HOTKEYS);
	}
}
