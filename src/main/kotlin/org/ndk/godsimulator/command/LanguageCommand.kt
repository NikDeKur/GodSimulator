package org.ndk.godsimulator.command

import org.bukkit.OfflinePlayer
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.database.Database.Companion.accessorAsync
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.CommandTabExecution
import org.ndk.minecraft.extension.sendLangMsg
import org.ndk.minecraft.language.MSGHolder

class LanguageCommand : SimulatorCommand() {
    override fun onCommand(execution: CommandExecution) {
        val language = execution.getLanguage(0)
        val sender = execution.sender
        if (sender is OfflinePlayer) {
            sender.accessorAsync.thenAccept {
                it.profile.language = language
            }
        }

        execution.sendLangMsg(MSG.CMD_LANGUAGE_SUCCESS, "lang" to language)
    }

    override fun onTabComplete(execution: CommandTabExecution): MutableList<String>? {
        return if (execution.argsSize == 1)
            ArrayList(GodSimulator.instance.languagesManager.languageCodes.map { it.code })
        else
            null
    }

    override val permissionNode: String = "adm.language"
    override val isConsoleFriendly: Boolean = false
    override val argsRequirement: Int = 1

    override val usageMSG: MSGHolder = MSG.CMD_LANGUAGE_USAGE

    override val name: String = "language"
}
