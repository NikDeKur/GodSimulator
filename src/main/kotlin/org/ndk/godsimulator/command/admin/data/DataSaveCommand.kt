package org.ndk.godsimulator.command.admin.data

import org.ndk.godsimulator.GodSimulator.Companion.database
import org.ndk.godsimulator.command.SimulatorCommand
import org.ndk.godsimulator.database.Database.Companion.accessorAsync
import org.ndk.godsimulator.language.MSG
import org.ndk.klib.format
import org.ndk.minecraft.plugin.ServerPlugin
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.CommandTabExecution
import org.ndk.minecraft.extension.nanosToMs
import org.ndk.minecraft.extension.sendLangMsg
import org.ndk.minecraft.language.MSGHolder

class DataSaveCommand : SimulatorCommand() {
    override fun onCommand(execution: CommandExecution) {
        val start = System.nanoTime()
        if (execution.args.isEmpty()) {
            database.saveCachedPlayersDataAsync()
                .thenAccept {
                    val time = (System.nanoTime() - start).nanosToMs().format(2)
                    execution.sendLangMsg(MSG.CMD_ADMIN_DATA_SAVE_SUCCESS, "time" to time)
                }
        } else {
            val player = execution.getOnlinePlayer(0)
            player.accessorAsync
                .thenApply { it.saveData() }
                .thenAccept {
                    val time = (System.nanoTime() - start).nanosToMs().format(2)
                    execution.sendLangMsg(
                        MSG.CMD_ADMIN_DATA_SAVE_PLAYER_SUCCESS,
                        "player" to player,
                        "time" to time
                    )
                }
        }
    }

    override fun onTabComplete(execution: CommandTabExecution): MutableList<String>? {
        return if (execution.args.size == 1) {
            ServerPlugin.onlineNames
        } else {
            null
        }
    }

    override val permissionNode: String = "cmd.data.save"
    override val isConsoleFriendly: Boolean = true
    override val argsRequirement: Int = 0

    override val usageMSG: MSGHolder? = null

    override val name: String = "save"
}