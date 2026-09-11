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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;

/**
 * Checks whether a chest state can be placed without creating a partnerless half of a
 * large chest, ie. the partner half must already exist in the world with a matching
 * facing and either a single or the opposite chest type.
 *
 * Placing a left/right chest while its partner half is missing leaves behind a
 * "half of a large chest", which also breaks hopper access to the completed large
 * chest until the chunk gets reloaded (litematica issue #241).
 *
 * Skipping the chest type in that case leaves the vanilla placement state (a single
 * chest), which the other half will still merge into a proper large chest via the
 * vanilla neighbor shape updates once it's placed.
 */
public class ChestPartnerCheck
{
	private ChestPartnerCheck()
	{
	}

	public static boolean hasValidChestPartner(Level world, BlockPos pos, BlockState chestState)
	{
		if (chestState.hasProperty(BlockStateProperties.CHEST_TYPE) == false ||
			chestState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) == false)
		{
			return true;
		}

		ChestType type = chestState.getValue(BlockStateProperties.CHEST_TYPE);

		if (type == ChestType.SINGLE)
		{
			return true;
		}

		Direction facing = chestState.getValue(BlockStateProperties.HORIZONTAL_FACING);
		Direction partnerDir = type == ChestType.LEFT ? facing.getClockWise() : facing.getCounterClockWise();
		BlockState partnerState = world.getBlockState(pos.relative(partnerDir));

		if (partnerState.is(chestState.getBlock()) == false ||
			partnerState.hasProperty(BlockStateProperties.CHEST_TYPE) == false ||
			partnerState.getValue(BlockStateProperties.HORIZONTAL_FACING) != facing)
		{
			return false;
		}

		ChestType partnerType = partnerState.getValue(BlockStateProperties.CHEST_TYPE);

		return partnerType == ChestType.SINGLE || partnerType == type.getOpposite();
	}
}
