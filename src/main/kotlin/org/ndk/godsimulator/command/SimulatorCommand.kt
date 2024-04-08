package org.ndk.godsimulator.command

import org.bukkit.entity.Player
import org.ndk.godsimulator.database.Database.Companion.accessor
import org.ndk.minecraft.command.ServerCommand
import org.ndk.minecraft.command.ServerRootCommand

abstract class SimulatorCommand : ServerCommand() {
    override fun resetCooldown(player: Player, commandPath: String) {
        return player.accessor.resetCooldown(commandPath)
    }

    override fun setCooldown(player: Player, commandPath: String, cooldown: Long) {
        return player.accessor.setCooldown(commandPath, cooldown)
    }

    override fun getCooldown(player: Player, commandPath: String): Long? {
        return player.accessor.getCooldown(commandPath)
    }



    abstract class Root : ServerRootCommand() {

        override fun resetCooldown(player: Player, commandPath: String) {
            return player.accessor.resetCooldown(commandPath)
        }

        override fun setCooldown(player: Player, commandPath: String, cooldown: Long) {
            return player.accessor.setCooldown(commandPath, cooldown)
        }

        override fun getCooldown(player: Player, commandPath: String): Long? {
            return player.accessor.getCooldown(commandPath)
        }
    }
}