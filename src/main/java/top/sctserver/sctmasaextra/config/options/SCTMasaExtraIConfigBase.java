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

package top.sctserver.sctmasaextra.config.options;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.util.StringUtils;
import top.sctserver.sctmasaextra.SCTMasaExtraMod;

/**
 * Base interface for SCTMasaExtra config options, following the TweakerMore convention:
 * the GUI display name is translated from {@code <modid>.config.<name>} and the comment
 * from {@code <modid>.config.<name>.comment}, instead of malilib's classic
 * {@code <prefix>.name.<name>} / {@code <prefix>.comment.<name>} scheme.
 *
 * <p>{@link #getConfigGuiDisplayName()} can live here as a default method, but
 * {@link #getComment()} cannot: malilib's {@code ConfigBase} already implements it
 * concretely, and a superclass method always takes precedence over interface defaults
 * ( TweakerMore solves this with a ConfigBaseMixin instead ). Implementors must
 * override {@link #getComment()} themselves, see the two wrapper classes in this package.
 */
public interface SCTMasaExtraIConfigBase extends IConfigBase
{
	String NAMESPACE_PREFIX = SCTMasaExtraMod.MOD_ID + ".config.";
	String COMMENT_SUFFIX = ".comment";

	@Override
	default String getConfigGuiDisplayName()
	{
		return StringUtils.translate(NAMESPACE_PREFIX + this.getName());
	}
}
