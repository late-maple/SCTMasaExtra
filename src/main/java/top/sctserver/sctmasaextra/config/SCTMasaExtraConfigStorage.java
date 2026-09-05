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

import java.io.File;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.util.data.json.JsonUtils;
import net.fabricmc.loader.api.FabricLoader;
import top.sctserver.sctmasaextra.SCTMasaExtraMod;

public class SCTMasaExtraConfigStorage implements IConfigHandler
{
	private static final SCTMasaExtraConfigStorage INSTANCE = new SCTMasaExtraConfigStorage();

	public static SCTMasaExtraConfigStorage getInstance()
	{
		return INSTANCE;
	}

	private static File getConfigFile()
	{
		return FabricLoader.getInstance().getConfigDir().resolve(SCTMasaExtraMod.MOD_ID + ".json").toFile();
	}

	@Override
	public void load()
	{
		File configFile = getConfigFile();

		if (configFile.exists() && configFile.isFile())
		{
			JsonElement element = JsonUtils.parseJsonFile(configFile.toPath());

			if (element instanceof JsonObject root)
			{
				ConfigUtils.readConfigBase(root, "Generic", SCTMasaExtraConfigs.getGenericOptions());
				ConfigUtils.readConfigBase(root, "Toggles", SCTMasaExtraConfigs.getToggleOptions());
			}
		}
	}

	@Override
	public void save()
	{
		JsonObject root = new JsonObject();

		ConfigUtils.writeConfigBase(root, "Generic", SCTMasaExtraConfigs.getGenericOptions());
		ConfigUtils.writeConfigBase(root, "Toggles", SCTMasaExtraConfigs.getToggleOptions());

		JsonUtils.writeJsonToFile(root, getConfigFile().toPath());
	}
}
