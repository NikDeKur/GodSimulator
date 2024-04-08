package org.ndk.godsimulator.command.admin.flag



import org.bukkit.command.CommandSender
import org.ndk.global.placeholders.Placeholder
import org.ndk.godsimulator.command.SimulatorCommand
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.language.sendFlagChange
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profile
import org.ndk.klib.toSingletonSet
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.CommandTabExecution
import org.ndk.minecraft.extension.getLangMsg
import kotlin.reflect.KMutableProperty1

abstract class FlagCommand : SimulatorCommand() {
    val usagePh by lazy {
        mapOf("flag" to Placeholder.ofSingle("name", name).toSingletonSet())
    }

    abstract val property: KMutableProperty1<PlayerProfile, Boolean>

    override fun onCommand(execution: CommandExecution) {
        val player = execution.player
        val profile = player.profile
        val state = execution.getBooleanOrNull(0) ?: !property.get(profile)
        property.set(profile, state)
        execution.sendFlagChange(name, state)
    }

    override fun onTabComplete(execution: CommandTabExecution): ArrayList<String>? {
        return if (execution.argsSize == 1) {
            TRUE_FALSE
        } else {
            null
        }
    }

    override val permissionNode by lazy {
        "cmd.flag.$name"
    }
    override val isConsoleFriendly: Boolean = false
    override val argsRequirement: Int = 0

    override val usageMSG = null
    override fun getUsage(sender: CommandSender) = sender.getLangMsg(MSG.CMD_SPECIFIC_FLAG_USAGE, usagePh).chatText


    companion object {
        private val _TRUE_FALSE = listOf("true", "false")
        val TRUE_FALSE: ArrayList<String>
            get() = ArrayList(_TRUE_FALSE)
    }
}