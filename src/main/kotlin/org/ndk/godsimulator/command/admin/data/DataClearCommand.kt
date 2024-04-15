package org.ndk.godsimulator.command.admin.data

import org.ndk.godsimulator.command.SimulatorCommand
import org.ndk.godsimulator.database.Database.accessorAsync
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.plugin.ServerPlugin
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.CommandTabExecution
import org.ndk.minecraft.extension.sendLangMsg


class DataClearCommand : SimulatorCommand() {
    override fun onCommand(execution: CommandExecution) {
        val target = execution.getOfflinePlayer(0)
        target.accessorAsync.thenAccept {
            it.clear()
            execution.sendLangMsg(MSG.CMD_ADMIN_DATA_CLEAR_SUCCESS, "player" to target)
        }
    }

    override fun onTabComplete(execution: CommandTabExecution): MutableList<String>? {
        return if (execution.args.size == 1) {
            ServerPlugin.offlineNames
        } else {
            null
        }
    }

    override val permissionNode: String = "cmd.data.clear"
    override val isConsoleFriendly: Boolean = true
    override val argsRequirement: Int = 1

    override val usageMSG = MSG.CMD_ADMIN_DATA_CLEAR_USAGE

    override val name: String = "clear"
}