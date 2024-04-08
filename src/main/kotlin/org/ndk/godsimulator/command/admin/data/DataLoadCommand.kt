package org.ndk.godsimulator.command.admin.data

import org.ndk.godsimulator.GodSimulator.Companion.database
import org.ndk.godsimulator.command.SimulatorCommand
import org.ndk.godsimulator.database.Database.Companion.accessorRaw
import org.ndk.godsimulator.language.MSG
import org.ndk.klib.format
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.CommandTabExecution
import org.ndk.minecraft.extension.nanosToMs
import org.ndk.minecraft.extension.sendLangMsg
import org.ndk.minecraft.language.MSGHolder
import org.ndk.minecraft.plugin.ServerPlugin

class DataLoadCommand : SimulatorCommand() {
    override fun onCommand(execution: CommandExecution) {
        if (execution.args.isEmpty()) {
            val start = System.nanoTime()
            database.loadCachedPlayersDataAsync()
                .thenAccept {
                    val finish = (System.nanoTime() - start).nanosToMs().format(2)
                    execution.sendLangMsg(MSG.CMD_ADMIN_DATA_LOAD_SUCCESS, "time" to finish)
                }
        } else {
            val player = execution.getOfflinePlayer(0)
            val start = System.nanoTime()
            val accessor = player.accessorRaw
            val wasLoaded = accessor.isFullyLoaded
            accessor.whenLoaded {
                if (wasLoaded) it.loadData()
                val finish = (System.nanoTime() - start).nanosToMs().format(2)
                execution.sendLangMsg(
                    MSG.CMD_ADMIN_DATA_LOAD_PLAYER_SUCCESS,
                    "player" to player,
                    "time" to finish
                )
            }
        }
    }

    override fun onTabComplete(execution: CommandTabExecution): MutableList<String>? {
        return if (execution.args.size == 1) {
            ServerPlugin.offlineNames
        } else {
            null
        }
    }

    override val permissionNode: String = "cmd.data.load"
    override val isConsoleFriendly: Boolean = true
    override val argsRequirement: Int = 0

    override val usageMSG: MSGHolder? = null

    override val name: String = "load"
}