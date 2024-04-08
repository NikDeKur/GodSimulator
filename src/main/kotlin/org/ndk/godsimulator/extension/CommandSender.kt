package org.ndk.godsimulator.extension

import org.bukkit.command.CommandSender
import org.ndk.godsimulator.language.SimulatorLangManager
import org.ndk.minecraft.extension.sendSimpleMessage


fun CommandSender.sendSimulatorMessage(message: String) {
    sendSimpleMessage(SimulatorLangManager.PREFIX + message)
}




