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

import fi.dy.masa.tweakeroo.config.Configs;
import net.minecraft.client.Minecraft;
import top.sctserver.sctmasaextra.config.SCTMasaExtraConfigs;

/**
 * Distinguishes "single click" from "hold" for the attack key while Tweakeroo's
 * {@code disableBlockBreakCooldown} is active in creative mode.
 *
 * The vanilla flow is: {@code Minecraft.startAttack()} runs once per press (and breaks the
 * first block via startDestroyBlock), then {@code Minecraft.continueAttack(attackDown)}
 * runs every tick with the current attack key state and drives continueDestroyBlock,
 * which is what Tweakeroo's cooldown removal accelerates. Tracking the {@code continueAttack}
 * argument gives an exact per-tick edge/level signal of the attack key without any
 * timestamp math: a press shorter than the hold threshold behaves like vanilla
 * (one broken block per click), while a longer hold lets the removed cooldown apply
 * for fast continuous breaking.
 */
public class BreakingClickHoldHandler
{
	private static boolean attackWasDownLastTick;
	private static int attackPressTicks;

	private BreakingClickHoldHandler()
	{
	}

	/**
	 * Called from the head of {@code Minecraft.continueAttack(boolean)}, exactly once per
	 * client tick while in game. {@code attackDown} is vanilla's own "attack key is held
	 * and breaking can happen" condition (it already includes screen/mouse-grab checks).
	 */
	public static void onContinueAttack(boolean attackDown)
	{
		if (attackDown)
		{
			// Rising edge: 0 on the press tick, then the number of ticks the key has been held
			attackPressTicks = attackWasDownLastTick ? attackPressTicks + 1 : 0;
		}
		else
		{
			attackPressTicks = 0;
		}

		attackWasDownLastTick = attackDown;
	}

	/**
	 * True while the current attack press is still within the "single click" window and
	 * {@code MultiPlayerGameMode.continueDestroyBlock} should behave like vanilla,
	 * i.e. keep the vanilla break cooldown instead of Tweakeroo's zeroed one.
	 */
	public static boolean shouldKeepBreakCooldown()
	{
		if (!SCTMasaExtraConfigs.CREATIVE_SINGLE_CLICK_BREAK_PROTECT.getBooleanValue())
		{
			return false;
		}

		// Only relevant when Tweakeroo actually removes the cooldown, and only in creative
		// where breaking is instant (the cooldown only ever applies to instabuild)
		if (!Configs.Disable.DISABLE_BLOCK_BREAK_COOLDOWN.getBooleanValue())
		{
			return false;
		}

		Minecraft mc = Minecraft.getInstance();

		if (mc.player == null || !mc.player.isCreative())
		{
			return false;
		}

		return attackPressTicks < SCTMasaExtraConfigs.CREATIVE_BREAK_HOLD_THRESHOLD_TICKS.getIntegerValue();
	}
}
