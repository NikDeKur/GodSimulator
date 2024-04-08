package org.ndk.godsimulator.command

import org.ndk.godsimulator.equipable.EquipableGUI
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.CommandTabExecution
import org.ndk.minecraft.language.MSGHolder

class PetsCommand : SimulatorCommand() {
    override fun onCommand(execution: CommandExecution) {
        val player = execution.player
        val gui = EquipableGUI.pets(player)
        gui.open()
    }

    override fun onTabComplete(execution: CommandTabExecution): MutableList<String>? = null

    override val permissionNode: String = "cmd.pets"
    override val isConsoleFriendly: Boolean = false
    override val argsRequirement: Int = 0

    override val usageMSG: MSGHolder = MSG.INTERNAL_ERROR

    override val name: String = "pets"
}