package org.ndk.godsimulator.command.admin

import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent
import org.ndk.godsimulator.command.SimulatorCommand
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.world.WorldsManager
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.CommandTabExecution
import org.ndk.minecraft.extension.sendLangMsg

class AdminHubCommand : SimulatorCommand() {
    override fun onCommand(execution: CommandExecution) {
        val executor = execution.sender
        val player = if (execution.argsSize > 0) {
            execution.getOnlinePlayer(0)
        } else {
            if (executor !is Player) {
                execution.sendLangMsg(MSG.ONLY_FOR_PLAYERS_SYNTAX)
                return
            } else {
                executor
            }
        }

        val worldName = player.world.name
        val worldData = WorldsManager.getWorldData(worldName) ?: kotlin.run {
            execution.internalError("WorldData not found for world $worldName")
            return
        }
        player.teleport(worldData.spawnLocation, PlayerTeleportEvent.TeleportCause.COMMAND)

        if (player == executor) {
            player.sendLangMsg(MSG.CMD_ADMIN_HUB_SUCCESS_SELF)
        } else {
            executor.sendLangMsg(MSG.CMD_ADMIN_HUB_SUCCESS_OTHER, "player" to player)
        }
    }

    override fun onTabComplete(execution: CommandTabExecution) = execution.online()

    override val permissionNode: String = "cmd.admin.hub"
    override val isConsoleFriendly: Boolean = true
    override val argsRequirement: Int = 0

    override val usageMSG = MSG.CMD_ADMIN_HUB_USAGE

    override val name = "hub"
}