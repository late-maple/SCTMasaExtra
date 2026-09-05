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

import fi.dy.masa.malilib.util.StringUtils;

public enum ConfigGuiTab
{
	GENERIC("sctmasaextra.gui.button.config_gui.generic"),
	TOGGLES("sctmasaextra.gui.button.config_gui.toggles");

	private final String translationKey;

	ConfigGuiTab(String translationKey)
	{
		this.translationKey = translationKey;
	}

	public String getDisplayName()
	{
		return StringUtils.translate(this.translationKey);
	}
}
