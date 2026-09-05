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

package top.sctserver.sctmasaextra.mixins.tweakeroo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import fi.dy.masa.tweakeroo.tweaks.PlacementTweaks;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import top.sctserver.sctmasaextra.config.SCTMasaExtraConfigs;

@Mixin(PlacementTweaks.class)
public abstract class MixinPositionAllowedByBreakingRestriction
{
	// the call site is erased to isAllowed(Ljava/lang/Object;)Z (UsageRestriction<TYPE> generic)
	@ModifyExpressionValue(
			method = "isPositionAllowedByBreakingRestriction",
			at = @At(
					value = "INVOKE",
					target = "Lfi/dy/masa/malilib/util/restrictions/BlockRestriction;isAllowed(Ljava/lang/Object;)Z"))
	private static boolean sctmasaextra$creativeBypassBlockTypeBreakRestriction(boolean original)
	{
		Minecraft mc = Minecraft.getInstance();

		if (mc.player != null &&
			SCTMasaExtraConfigs.CREATIVE_DISABLE_BLOCK_TYPE_BREAK_RESTRICTION.getBooleanValue() &&
			mc.player.isCreative())
		{
			return true;
		}

		return original;
	}
}
