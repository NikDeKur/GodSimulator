package org.ndk.godsimulator.command

import org.ndk.godsimulator.menu.ProfilesGUI
import org.ndk.minecraft.plugin.ServerPlugin
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.CommandTabExecution
import org.ndk.minecraft.language.MSGHolder


class ProfilesCommand : SimulatorCommand() {
    override fun onCommand(execution: CommandExecution) {
        val player = execution.player
        ProfilesGUI(player).open()
    }

    override fun onTabComplete(execution: CommandTabExecution): MutableList<String>? {
        return if (execution.player.hasPermission("adm.profiles")) {
            ServerPlugin.offlineNames
        } else null
    }

    override val permissionNode: String = "cmd.profiles"
    override val isConsoleFriendly: Boolean = false
    override val argsRequirement: Int = 0

    override val usageMSG: MSGHolder? = null

    override val name: String = "profiles"
}