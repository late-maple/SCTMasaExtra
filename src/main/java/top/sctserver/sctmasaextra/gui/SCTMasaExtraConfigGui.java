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

package top.sctserver.sctmasaextra.gui;

import java.util.List;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import top.sctserver.sctmasaextra.SCTMasaExtraMod;
import top.sctserver.sctmasaextra.config.ConfigGuiTab;
import top.sctserver.sctmasaextra.config.SCTMasaExtraConfigs;

public class SCTMasaExtraConfigGui extends GuiConfigsBase
{
	private static ConfigGuiTab tab = ConfigGuiTab.GENERIC;

	public SCTMasaExtraConfigGui()
	{
		super(10, 50, SCTMasaExtraMod.MOD_ID, null, "sctmasaextra.gui.title", SCTMasaExtraMod.MOD_VERSION);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.clearOptions();

		int x = 10;
		int y = 26;

		for (ConfigGuiTab tab : ConfigGuiTab.values())
		{
			x += this.createButton(x, y, -1, tab);
		}
	}

	private int createButton(int x, int y, int width, ConfigGuiTab tab)
	{
		ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getDisplayName());
		button.setEnabled(SCTMasaExtraConfigGui.tab != tab);
		this.addButton(button, new ButtonListener(tab, this));

		return button.getWidth() + 2;
	}

	@Override
	public List<ConfigOptionWrapper> getConfigs()
	{
		if (tab == ConfigGuiTab.GENERIC)
		{
			return ConfigOptionWrapper.createFor(SCTMasaExtraConfigs.getGenericOptions());
		}
		else if (tab == ConfigGuiTab.TOGGLES)
		{
			return ConfigOptionWrapper.createFor(SCTMasaExtraConfigs.getToggleOptions());
		}

		return ConfigOptionWrapper.createFor(SCTMasaExtraConfigs.getAllOptions());
	}

	public static void openGui()
	{
		GuiBase.openGui(new SCTMasaExtraConfigGui());
	}

	private record ButtonListener(ConfigGuiTab tab, SCTMasaExtraConfigGui parent) implements IButtonActionListener
	{
		@Override
		public void actionPerformedWithButton(ButtonBase button, int mouseButton)
		{
			SCTMasaExtraConfigGui.tab = this.tab;
			this.parent.reCreateListWidget(); // apply the new config width
			if (this.parent.getListWidget() != null)
			{
				this.parent.getListWidget().resetScrollbarPosition();
			}
			this.parent.initGui();
		}
	}
}
