package org.ndk.godsimulator.command

import com.sk89q.worldedit.IncompleteRegionException
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.world.World
import org.ndk.godsimulator.GodSimulator.Companion.worldedit
import org.ndk.godsimulator.extension.sendSimulatorMessage
import org.ndk.godsimulator.extension.toWEVector
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.wobject.building.BuildingsManager
import org.ndk.klib.format
import org.ndk.klib.measureAverageTime
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.CommandTabExecution
import org.ndk.minecraft.extension.nanosToMs

class SaveBuildingCommand : SimulatorCommand() {
    override fun onCommand(execution: CommandExecution) {
        val player = execution.player
        val yaw = player.location.yaw
        // Cancel command if player is not looking to the east
        if (yaw > -45 || yaw < -135) {
            player.sendSimulatorMessage("&cВы должны смотреть на восток!")
        }
        val time = measureAverageTime(1) {
            val wgPlayer = worldedit.wrapPlayer(player)
            val selection = try {
                val session = WorldEdit.getInstance().sessionManager[wgPlayer]
                session.getSelection(wgPlayer.world as World)
            } catch (e: IncompleteRegionException) {
                player.sendSimulatorMessage("&cВыделите область с помощью WorldEdit!")
                return
            }
            val origin = player.location.toWEVector()
            origin.x += 2
            BuildingsManager.saveBuilding(selection, origin, execution.getArg(0))
        }
        player.sendSimulatorMessage("&aСхематика успешно сохранена! &7(${time.nanosToMs().format(2)} ms)")
    }

    override fun onTabComplete(execution: CommandTabExecution): MutableList<String>? {
        return null
    }

    override val permissionNode: String = "cmd.savebuilding"
    override val isConsoleFriendly: Boolean = false
    override val argsRequirement: Int = 1

    override val usageMSG = MSG.CMD_SAVEBUILDING_USAGE

    override val name: String = "savebuilding"
}