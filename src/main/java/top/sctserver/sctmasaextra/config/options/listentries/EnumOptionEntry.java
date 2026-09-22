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

package top.sctserver.sctmasaextra.config.options.listentries;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

import java.util.Arrays;

/**
 * Base interface for enums used as {@link IConfigOptionListEntry} values, following
 * the TweakerMore convention: value display names are translated from
 * {@code <translationPrefix><value_name>} keys, e.g. {@code sctmasaextra.list_entry.<name>.<value>}.
 */
public interface EnumOptionEntry extends IConfigOptionListEntry
{
	String name();

	int ordinal();

	/**
	 * Redirect this to {@code Enum.values()}
	 */
	EnumOptionEntry[] getAllValues();

	/**
	 * The default/fallback value
	 */
	EnumOptionEntry getDefault();

	/**
	 * The translation key prefix, e.g. "sctmasaextra.list_entry.my_value_type."
	 */
	String getTranslationPrefix();

	@Override
	default String getStringValue()
	{
		return this.name().toLowerCase();
	}

	@Override
	default String getDisplayName()
	{
		return StringUtils.translate(this.getTranslationPrefix() + this.name().toLowerCase());
	}

	@Override
	default IConfigOptionListEntry cycle(boolean forward)
	{
		int index = this.ordinal();
		EnumOptionEntry[] values = this.getAllValues();

		index += forward ? 1 : -1;
		index = (index + values.length) % values.length;

		return values[index];
	}

	@Override
	default IConfigOptionListEntry fromString(String value)
	{
		return Arrays.stream(this.getAllValues()).
				filter(o -> o.name().equalsIgnoreCase(value)).
				findFirst().
				orElseGet(this::getDefault);
	}
}
