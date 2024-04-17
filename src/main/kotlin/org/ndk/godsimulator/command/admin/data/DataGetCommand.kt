package org.ndk.godsimulator.command.admin.data

import org.bukkit.entity.Player
import org.ndk.godsimulator.command.SimulatorCommand
import org.ndk.godsimulator.database.Database.accessor
import org.ndk.godsimulator.database.Database.accessorAsync
import org.ndk.godsimulator.database.GSON
import org.ndk.godsimulator.language.MSG
import org.ndk.klib.isBlankOrEmpty
import org.ndk.klib.removeEmpty
import org.ndk.minecraft.Utils.debug
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.CommandTabExecution
import org.ndk.minecraft.extension.sendSimpleMessage
import org.ndk.minecraft.plugin.ServerPlugin

class DataGetCommand : SimulatorCommand() {

    override fun onCommand(execution: CommandExecution) {
        val target = execution.getOfflinePlayer(0)
        val path = execution.getArgOrNull(1) ?: ""
        target.accessorAsync.thenAccept {
            try {
                var data = it[path]
                if (data is MutableMap<*, *>) {
                    data = HashMap(data)
                    data.removeEmpty()
                    data =
                        if (data.isEmpty()) "<EmptyMap>"
                        else GSON.gson.toJson(data)
                }

                execution.send(
                    MSG.CMD_ADMIN_DATA_GET_SUCCESS,
                    "player" to target,
                    "path" to path
                )
                execution.sendSimpleMessage(data.toString())
            } catch (e: Throwable) {
                debug("Error while getting data: $e")
                e.printStackTrace()
            }
        }
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


    override val permissionNode: String = "cmd.data.get"
    override val isConsoleFriendly: Boolean = true
    override val argsRequirement: Int = 1

    override val usageMSG = MSG.CMD_ADMIN_DATA_GET_USAGE

    override val name: String = "get"
}