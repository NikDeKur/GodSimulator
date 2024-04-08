package org.ndk.godsimulator.command.admin.data

import org.bukkit.command.CommandSender
import org.ndk.godsimulator.command.SimulatorCommand
import org.ndk.godsimulator.language.MSG

class DataBaseCommand : SimulatorCommand.Root() {

    override val permissionNode: String = "cmd.admin.data"
    override val isConsoleFriendly: Boolean = true

    override val children = arrayOf(
        DataLoadCommand(),
        DataSaveCommand(),

        DataGetCommand(),
        DataSetCommand(),
        DataRemoveCommand(),
        DataClearCommand()
    )

    override val usageMSG = MSG.CMD_ADMIN_DATA_USAGE

    override val name: String = "data"
}