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

package top.sctserver.sctmasaextra.mixins.minecraft;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.spectator.PlayerMenuItem;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import net.minecraft.client.gui.spectator.categories.TeleportToPlayerMenuCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.sctserver.sctmasaextra.config.SCTMasaExtraConfigs;
import top.sctserver.sctmasaextra.util.FakePlayerFilter;

/**
 * Removes fake players (e.g. Carpet /player bots) from the spectator menu's main
 * teleport list (the Teleport to Player page) by rebuilding the final items list
 * without them.
 *
 * The no-arg constructor is the only entry point for the main list; the public
 * Collection constructor also builds the member pages of the Teleport to Team menu
 * (TeleportToTeamMenuCategory), which must keep its members so players can still
 * teleport to e.g. fake players through their team. Injecting only the no-arg
 * constructor also guarantees ordering: the Collection constructor (including other
 * mods' TAIL handlers that re-append spectator entries, e.g. TweakerMore's
 * spectatorTeleportMenuIncludeSpectator) has fully run by the time this TAIL executes,
 * so those entries are filtered here as well, without any priority games.
 *
 * The filtering works off the entry's display name only, deliberately without an
 * accessor mixin and without lambdas: an accessor interface class load gets triggered
 * by the constructor's own lambda bootstrap before this handler runs (before the
 * accessor could be applied to PlayerMenuItem), which crashes with IllegalClassLoadError.
 */
@Mixin(TeleportToPlayerMenuCategory.class)
public abstract class MixinTeleportToPlayerMenuCategory
{
	@Shadow
	@Final
	@Mutable
	private List<SpectatorMenuItem> items;

	@Inject(method = "<init>()V", at = @At("TAIL"))
	private void sctmasaextra$removeFakePlayerEntries(CallbackInfo ci)
	{
		if (!SCTMasaExtraConfigs.SPECTATOR_TELEPORT_MENU_FILTER_FAKE_PLAYERS.getBooleanValue() || this.items.isEmpty())
		{
			return;
		}

		List<SpectatorMenuItem> filtered = new ArrayList<>(this.items.size());

		for (SpectatorMenuItem item : this.items)
		{
			if (item instanceof PlayerMenuItem && FakePlayerFilter.isFakePlayer(item.getName().getString()))
			{
				continue;
			}

			filtered.add(item);
		}

		this.items = filtered;
	}
}
