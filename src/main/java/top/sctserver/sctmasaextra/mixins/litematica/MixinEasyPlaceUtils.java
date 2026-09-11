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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.litematica.util.EasyPlaceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import top.sctserver.sctmasaextra.config.SCTMasaExtraConfigs;
import top.sctserver.sctmasaextra.util.ChestPartnerCheck;

import java.util.List;

/**
 * Encode side: while packing the easy place protocol V3 value, request a plain single
 * chest instead of a left/right chest while its partner half is not in the world yet
 * (litematica issue #241). The server (including older Servux versions) just applies
 * the requested value, so fixing the request alone fixes both single player and
 * dedicated servers. The protocol bit width stays the same, only the value changes.
 */
@Mixin(EasyPlaceUtils.class)
public abstract class MixinEasyPlaceUtils
{
	@WrapOperation(method = "applyPlacementProtocolV3",
				  at = @At(value = "INVOKE", target = "Ljava/util/List;indexOf(Ljava/lang/Object;)I"))
	private static <T extends Comparable<T>> int sctmasaextra$requestSingleChestWithoutPartner(List<T> list, Object value,
																							   Operation<Integer> original,
																							   BlockPos pos, BlockState state)
	{
		if (value instanceof ChestType chestType && chestType != ChestType.SINGLE &&
			SCTMasaExtraConfigs.EASY_PLACE_INCOMPLETE_LARGE_CHEST_FIX.getBooleanValue())
		{
			Level world = Minecraft.getInstance().level;

			if (world != null && ChestPartnerCheck.hasValidChestPartner(world, pos, state) == false)
			{
				return original.call(list, ChestType.SINGLE);
			}
		}

		return original.call(list, value);
	}
}
