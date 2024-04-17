package org.ndk.godsimulator.command

import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.wobject.building.BuildingsManager
import org.ndk.godsimulator.wobject.building.WorldEditAPI
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.CommandTabExecution
import org.ndk.minecraft.extension.sendSimpleMessage


class SpawnBuildingCommand : SimulatorCommand() {
    override fun onCommand(execution: CommandExecution) {
        val player = execution.player
        val cardinal = execution.getIntOrNull(1) ?: 0
        val side = WorldEditAPI.WorldSide.entries[cardinal]
        val region = BuildingsManager.pasteBuilding(player.location, execution.getArg(0), side)
        player.sendSimpleMessage("&aСхематика успешно установлена &7(${region.minimumPoint} - ${region.maximumPoint})")
        player.sendSimpleMessage("${BuildingsManager.getBuildings(player, player.location)}")

    }

    override fun onTabComplete(execution: CommandTabExecution): MutableList<String>? {
        return null
    }

    override val permissionNode: String = "cmd.test2"
    override val isConsoleFriendly: Boolean = false
    override val argsRequirement: Int = 1

    override val usageMSG = MSG.CMD_SPAWNBUILDING_USAGE

    override val name: String = "spawnbuilding"
}