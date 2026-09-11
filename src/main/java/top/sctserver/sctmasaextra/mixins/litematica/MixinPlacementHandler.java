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

package top.sctserver.sctmasaextra.mixins.litematica;

import fi.dy.masa.litematica.util.PlacementHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.sctserver.sctmasaextra.config.SCTMasaExtraConfigs;
import top.sctserver.sctmasaextra.util.ChestPartnerCheck;

/**
 * Decode side: don't let the easy place protocol V3 leave a partnerless half chest
 * behind (litematica issue #241). Legitimate states that already carry a left/right
 * type from vanilla placement always have their partner in the world and pass the
 * check unchanged.
 */
@Mixin(PlacementHandler.class)
public abstract class MixinPlacementHandler
{
	@Inject(method = "applyPlacementProtocolV3", at = @At("RETURN"))
	private static void sctmasaextra$demotePartnerlessHalfLargeChest(BlockState state, PlacementHandler.UseContext context,
																CallbackInfoReturnable<BlockState> cir)
	{
		BlockState result = cir.getReturnValue();

		if (result == null ||
			SCTMasaExtraConfigs.EASY_PLACE_INCOMPLETE_LARGE_CHEST_FIX.getBooleanValue() == false ||
			result.hasProperty(BlockStateProperties.CHEST_TYPE) == false)
		{
			return;
		}

		if (result.getValue(BlockStateProperties.CHEST_TYPE) != ChestType.SINGLE &&
			ChestPartnerCheck.hasValidChestPartner(context.world(), context.pos(), result) == false)
		{
			cir.setReturnValue(result.setValue(BlockStateProperties.CHEST_TYPE, ChestType.SINGLE));
		}
	}
}
