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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import top.sctserver.sctmasaextra.config.SCTMasaExtraConfigs;

/**
 * Removes the vanilla "requires elevated permissions" command confirmation window
 * (multiplayer.confirm_command.permissions_required) shown when a command is run by
 * clicking a run_command chat component or a server dialog: the wrapped call is
 * replaced with the NO_ISSUES branch (send the command, restore the screen).
 *
 * verifyCommand() itself is not touched because its CommandCheckResult enum is
 * package-private; the permissions branch is told apart from the parse-error and
 * signature-required ones by its translation key. The server still rejects the
 * command as usual if the permission is genuinely missing.
 */
@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener
{
	private static final String PERMISSIONS_REQUIRED_KEY = "multiplayer.confirm_command.permissions_required";

	@WrapOperation(
			method = "sendUnattendedCommand",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;openCommandSendConfirmationWindow(Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/client/gui/screens/Screen;)V"))
	private void sctmasaextra$sendInsteadOfConfirmingPermission(ClientPacketListener receiver, String command,
																String confirmKey, Screen screen,
																Operation<Void> original)
	{
		if (SCTMasaExtraConfigs.SKIP_COMMAND_PERMISSION_CONFIRM.getBooleanValue() &&
			PERMISSIONS_REQUIRED_KEY.equals(confirmKey))
		{
			receiver.send(new ServerboundChatCommandPacket(command));
			Minecraft.getInstance().gui.setScreen(screen);
			return;
		}

		original.call(receiver, command, confirmKey, screen);
	}
}
