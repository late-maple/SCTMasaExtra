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

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.sctserver.sctmasaextra.util.BreakingClickHoldHandler;

@Mixin(MultiPlayerGameMode.class)
public abstract class MixinMultiPlayerGameMode
{
	/**
	 * While the attack key press is still a "single click", act like the vanilla
	 * cooldown branch ({@code destroyDelay > 0} -> return true without breaking), so that
	 * Tweakeroo's removed break cooldown only applies once the key is genuinely held.
	 * Returning true (and not touching destroyDelay) keeps this independent of the
	 * callback order against Tweakeroo's own continueDestroyBlock handler, which only
	 * zeroes destroyDelay and never forces a break.
	 */
	@Inject(method = "continueDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z",
			at = @At("HEAD"), cancellable = true)
	private void sctmasaextra$keepBreakCooldownDuringSingleClick(BlockPos pos, Direction side,
																 CallbackInfoReturnable<Boolean> cir)
	{
		if (BreakingClickHoldHandler.shouldKeepBreakCooldown())
		{
			cir.setReturnValue(true);
		}
	}
}
