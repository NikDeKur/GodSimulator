package org.ndk.godsimulator.command.admin

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.ndk.godsimulator.command.SimulatorCommand
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.CommandTabExecution
import org.ndk.minecraft.extension.cancel
import org.ndk.minecraft.extension.getBooleanTag
import org.ndk.minecraft.item.ItemPattern
import org.ndk.minecraft.language.MSGHolder

class AdminStickCommand : SimulatorCommand() {
    override fun onCommand(execution: CommandExecution) {
        val player = execution.player
        val stick = PATTERN.build(player)
        player.inventory.addItem(stick)
    }

    override fun onTabComplete(execution: CommandTabExecution): MutableList<String>? = null
    override val permissionNode: String = "cmd.admin.stick"
    override val isConsoleFriendly: Boolean = false
    override val argsRequirement: Int = 0

    override val usageMSG: MSGHolder? = null

    override val name: String = "stick"

    companion object {
        val PATTERN = ItemPattern.from(Material.STICK)
            .setDisplayName(MSG.ADMIN_STICK_DISPLAY)
            .setTag("isAdminStick", true)
            .setUnstackable()


        object StickListener : Listener {
            @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
            fun onStickClick(event: EntityDamageByEntityEvent) {
                val attacker = event.damager as? Player ?: return
                if (!attacker.hasPermission("cmd.admin.stick")) return
                val itemInHand = attacker.inventory.itemInMainHand
                val isAdminStick = itemInHand.getBooleanTag("isAdminStick") ?: return
                if (!isAdminStick) return
                val entity = event.entity
                event.cancel()
                entity.remove()
            }
        }
    }
}