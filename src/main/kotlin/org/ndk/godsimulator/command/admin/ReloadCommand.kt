package org.ndk.godsimulator.command.admin

import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.command.SimulatorCommand
import org.ndk.godsimulator.extension.sendSimulatorMessage
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.CommandTabExecution
import org.ndk.minecraft.language.MSGHolder
import kotlin.system.measureTimeMillis

class ReloadCommand : SimulatorCommand() {
    override fun onCommand(execution: CommandExecution) {
        val time = measureTimeMillis {
            GodSimulator.instance.reload()
        }
        execution.sendSimulatorMessage("&aPlugin reloaded in &e$time ms")
    }

    override fun onTabComplete(execution: CommandTabExecution): MutableList<String>? {
        return null
    }

    override val permissionNode: String = "cmd.reload"
    override val isConsoleFriendly: Boolean = true
    override val argsRequirement: Int = 0

    override val usageMSG: MSGHolder? = null

    override val name: String
        get() = "reload"
}