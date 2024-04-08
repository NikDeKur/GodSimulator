package org.ndk.godsimulator.command.admin.data

import org.bukkit.entity.Player
import org.ndk.godsimulator.command.SimulatorCommand
import org.ndk.godsimulator.database.Database.Companion.accessor
import org.ndk.godsimulator.database.Database.Companion.accessorAsync
import org.ndk.godsimulator.language.MSG
import org.ndk.klib.isBlankOrEmpty
import org.ndk.minecraft.plugin.ServerPlugin
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.CommandTabExecution
import org.ndk.minecraft.extension.sendLangMsg

class DataRemoveCommand : SimulatorCommand() {
    override fun onCommand(execution: CommandExecution) {
        val target = execution.getOfflinePlayer(0)
        val path = execution.getArg(1)
        target.accessorAsync.thenAccept {
            it[path] = null
        }

        execution.sendLangMsg(
            MSG.CMD_ADMIN_DATA_REMOVE_SUCCESS,
            "player" to target,
            "path" to path)

    }

    override fun onTabComplete(execution: CommandTabExecution): MutableList<String>? {
        return when (execution.args.size) {
            1 -> ServerPlugin.offlineNames
            2 -> {
                val player = execution.getOfflinePlayerOrNull(0) ?: return null
                val path = execution.getArgOrNull(1) ?: return null
                val completedPath = path.split(".").dropLast(1).joinToString(".")
                if (player !is Player) return null
                val atPath = player.accessor[completedPath] ?: return null

                if (atPath !is Map<*, *>) return null

                atPath.keys.map {
                    if (!completedPath.isBlankOrEmpty())
                        "$completedPath.${it.toString()}"
                    else
                        it.toString()
                } as ArrayList<String>
            }
            else -> null
        }
    }

    override val permissionNode: String = "cmd.data.remove"
    override val isConsoleFriendly: Boolean = true
    override val argsRequirement: Int = 2

    override val usageMSG = MSG.CMD_ADMIN_DATA_REMOVE_USAGE

    override val name: String = "remove"
}