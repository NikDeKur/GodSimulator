package org.ndk.godsimulator.god

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.GodSimulator.Companion.scheduler
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.world.WorldsManager.data
import org.ndk.minecraft.movement.MovementManager

class ForceGodSelectGUI(player: Player, val profile: PlayerProfile) : GodSelectGUI(player) {

    init {
        val spawnLocation = player.world.data.spawnLocation
        if (player.location != spawnLocation) {
            val movementManager = GodSimulator.modulesManager.getModule("MovementManager") as MovementManager
            movementManager.teleport(player, spawnLocation)
        }
    }

    override fun open() {
        scheduler.runTask {
            super.open()
        }
    }

    override fun onGodSelect(god: God) {
        profile.stopForceSelectGod(god)
        closeAndFinish()
    }

    override fun onClose(event: InventoryCloseEvent) {
        if (profile.forceSelectGod) {
            finish()
            scheduler.runTask {
                if (player.isOnline) {
                    ForceGodSelectGUI(player, profile).open()
                }
            }
        }
    }
}