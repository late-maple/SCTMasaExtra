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

import top.sctserver.sctmasaextra.config.options.listentries.EnumOptionEntry;

public enum SpectatorTeleportMenuFilterMode implements EnumOptionEntry
{
	TEAM,
	PREFIX,
	SUFFIX;

	public static final SpectatorTeleportMenuFilterMode DEFAULT = TEAM;

	@Override
	public EnumOptionEntry[] getAllValues()
	{
		return values();
	}

	@Override
	public EnumOptionEntry getDefault()
	{
		return DEFAULT;
	}

	@Override
	public String getTranslationPrefix()
	{
		return "sctmasaextra.list_entry.spectatorTeleportMenuFilterMode.";
	}
}
