package org.ndk.godsimulator.command.admin.flag

import org.bukkit.command.CommandSender
import org.ndk.godsimulator.command.SimulatorCommand
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.command.ServerCommand
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.language.MSGHolder


class FlagBaseCommand : SimulatorCommand.Root() {

    override val permissionNode: String = "cmd.admin.flag"
    override val isConsoleFriendly: Boolean = true


    override val usageMSG: MSGHolder? = null
    override fun getUsage(sender: CommandSender): String {
        return sender.getLangMsg(MSG.CMD_ADMIN_FLAG_USAGE).chatText
    }

    override val name: String = "flag"
    override val children: Array<ServerCommand> = arrayOf(
        FlagLocationsCommand(), FlagSkillCastCommand(), FlagAdventureCommand(), FlagCooldownCommand()
    )
}