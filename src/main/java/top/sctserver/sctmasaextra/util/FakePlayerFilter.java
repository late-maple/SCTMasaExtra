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

package top.sctserver.sctmasaextra.util;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.world.scores.PlayerTeam;
import top.sctserver.sctmasaextra.config.SCTMasaExtraConfigs;
import top.sctserver.sctmasaextra.config.SpectatorTeleportMenuFilterMode;

/**
 * Decides whether a listed player is a fake player (e.g. a Carpet /player bot) for the
 * spectator main teleport list filter, based on the configured mode:
 * a player is fake when their team is in the team list, or their name starts with a
 * prefix / ends with a suffix from the corresponding list.
 *
 * Everything works off the player name only: the scoreboard resolves teams from member
 * names, so no player object access is needed at the mixin injection sites.
 */
public class FakePlayerFilter
{
	private FakePlayerFilter()
	{
	}

	public static boolean isFakePlayer(String playerName)
	{
		if (!SCTMasaExtraConfigs.SPECTATOR_TELEPORT_MENU_FILTER_FAKE_PLAYERS.getBooleanValue())
		{
			return false;
		}

		Minecraft mc = Minecraft.getInstance();

		if (mc.player != null && playerName.equals(mc.player.getGameProfile().name()))
		{
			return false;
		}

		SpectatorTeleportMenuFilterMode mode =
				(SpectatorTeleportMenuFilterMode) SCTMasaExtraConfigs.SPECTATOR_TELEPORT_MENU_FILTER_MODE.getOptionListValue();

		return switch (mode)
		{
			case TEAM -> isListedTeam(playerName, SCTMasaExtraConfigs.FAKE_PLAYER_TEAM_LIST.getStrings());
			case PREFIX -> containsAffix(SCTMasaExtraConfigs.FAKE_PLAYER_PREFIX_LIST.getStrings(), playerName, true);
			case SUFFIX -> containsAffix(SCTMasaExtraConfigs.FAKE_PLAYER_SUFFIX_LIST.getStrings(), playerName, false);
		};
	}

	private static boolean isListedTeam(String playerName, List<String> teamNames)
	{
		if (teamNames.isEmpty())
		{
			return false;
		}

		Minecraft mc = Minecraft.getInstance();

		if (mc.level == null)
		{
			return false;
		}

		// getPlayersTeam = member name -> team; getPlayerTeam in 26.2 looks up by team name instead
		PlayerTeam team = mc.level.getScoreboard().getPlayersTeam(playerName);

		return team != null && teamNames.contains(team.getName());
	}

	private static boolean containsAffix(List<String> affixes, String playerName, boolean prefix)
	{
		for (String affix : affixes)
		{
			// an empty affix would match every player name
			if (affix.isEmpty())
			{
				continue;
			}

			if (prefix ? playerName.startsWith(affix) : playerName.endsWith(affix))
			{
				return true;
			}
		}

		return false;
	}
}
