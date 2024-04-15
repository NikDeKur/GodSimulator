package org.ndk.godsimulator.command.admin.data
import org.bukkit.entity.Player
import org.ndk.global.tools.SimpleDataType
import org.ndk.godsimulator.command.SimulatorCommand
import org.ndk.godsimulator.database.Database.accessor
import org.ndk.godsimulator.database.Database.accessorAsync
import org.ndk.godsimulator.language.MSG
import org.ndk.klib.isBlankOrEmpty
import org.ndk.klib.toSingletonList
import org.ndk.minecraft.plugin.ServerPlugin
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.CommandTabExecution

class DataSetCommand : SimulatorCommand() {
    override fun onCommand(execution: CommandExecution) {
        val target = execution.getOfflinePlayer(0)
        val path = execution.getArg(1)
        val valueStr = execution.getArg(2)
        val type = execution.getDataType(3, SimpleDataType.STRING)
        val value = type.convert(valueStr)
        target.accessorAsync.thenAccept {
            val current = it[path]
            if (current != null && current.javaClass != value.javaClass) {
                execution.send(
                    MSG.CMD_ADMIN_DATA_SET_TYPE_MISMATCH,
                    "path" to path, "current" to current.javaClass.name, "new" to type
                )
                return@thenAccept
            }
            it[path] = value

            execution.send(
                MSG.CMD_ADMIN_DATA_SET_SUCCESS,
                "player" to target,
                "path" to path,
                "value" to valueStr,
                "type" to type
            )
        }
    }

    val valuePlaceholderList = "[value]".toSingletonList()
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
            3 -> valuePlaceholderList.toMutableList()
            4 -> SimpleDataType.entries.map { it.name } as ArrayList<String>
            else -> null
        }
    }

    override val permissionNode: String = "cmd.data.set"
    override val isConsoleFriendly: Boolean = true
    override val argsRequirement: Int = 3

    override val usageMSG = MSG.CMD_ADMIN_DATA_SET_USAGE

    override val name: String = "set"
}