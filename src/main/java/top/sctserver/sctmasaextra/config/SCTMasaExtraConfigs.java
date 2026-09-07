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
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import top.sctserver.sctmasaextra.SCTMasaExtraMod;
import top.sctserver.sctmasaextra.gui.SCTMasaExtraConfigGui;

public class SCTMasaExtraConfigs
{
	// malilib derives the translation keys "sctmasaextra.config.name.<option>",
	// "sctmasaextra.config.comment.<option>" and "sctmasaextra.config.prettyName.<option>" from this prefix
	public static final String CONFIG_TRANSLATION_PREFIX = SCTMasaExtraMod.MOD_ID + ".config";

	public static final ConfigHotkey OPEN_CONFIG_GUI = new ConfigHotkey("openConfigGui", "S,C").apply(CONFIG_TRANSLATION_PREFIX);

	public static final ConfigBoolean CREATIVE_DISABLE_BLOCK_TYPE_BREAK_RESTRICTION =
			new ConfigBoolean("creativeDisableBlockTypeBreakRestriction", true).apply(CONFIG_TRANSLATION_PREFIX);

	public static final ConfigBoolean CREATIVE_SINGLE_CLICK_BREAK_PROTECT =
			new ConfigBoolean("creativeSingleClickBreakProtect", true).apply(CONFIG_TRANSLATION_PREFIX);

	public static final ConfigInteger CREATIVE_BREAK_HOLD_THRESHOLD_TICKS =
			new ConfigInteger("creativeBreakHoldThresholdTicks", 3, 1, 20).apply(CONFIG_TRANSLATION_PREFIX);

	private static final List<IConfigBase> GENERIC_OPTIONS = ImmutableList.of(OPEN_CONFIG_GUI, CREATIVE_BREAK_HOLD_THRESHOLD_TICKS);
	private static final List<IConfigBase> TOGGLE_OPTIONS = ImmutableList.of(CREATIVE_DISABLE_BLOCK_TYPE_BREAK_RESTRICTION, CREATIVE_SINGLE_CLICK_BREAK_PROTECT);

	public static List<IConfigBase> getGenericOptions()
	{
		return GENERIC_OPTIONS;
	}

	public static List<IConfigBase> getToggleOptions()
	{
		return TOGGLE_OPTIONS;
	}

	public static List<IConfigBase> getAllOptions()
	{
		return ImmutableList.<IConfigBase>builder()
				.addAll(GENERIC_OPTIONS)
				.addAll(TOGGLE_OPTIONS)
				.build();
	}

	public static void initConfigs()
	{
		OPEN_CONFIG_GUI.getKeybind().setCallback((action, key) -> {
			SCTMasaExtraConfigGui.openGui();
			return true;
		});
	}
}
