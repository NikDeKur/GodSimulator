package org.ndk.godsimulator.command.admin

import org.bukkit.command.CommandSender
import org.ndk.godsimulator.command.SimulatorCommand
import org.ndk.godsimulator.command.admin.data.DataBaseCommand
import org.ndk.godsimulator.command.admin.flag.FlagBaseCommand
import org.ndk.godsimulator.extension.getLangMsg
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.extension.getLangMsg

class AdminBaseCommand : SimulatorCommand.Root() {

    override val permissionNode: String = "cmd.admin"
    override val isConsoleFriendly: Boolean = true

    override val usageMSG = MSG.CMD_ADMIN_BASE_USAGE

    override val children = arrayOf(
        ReloadCommand(), DataBaseCommand(), FlagBaseCommand(),
        AdminStickCommand(), AdminHubCommand(),
    )

    override val name: String = "admin"
}