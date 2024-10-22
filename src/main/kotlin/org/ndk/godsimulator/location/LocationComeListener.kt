package org.ndk.godsimulator.location


import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.ndk.godsimulator.extension.simulatorLocation
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.language.Quick
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profile
import org.ndk.godsimulator.utils.ConfirmationGUI
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.movement.OptiPlayerMoveEvent

object LocationComeListener : Listener {

    @EventHandler
    fun onPlayerMove(event: OptiPlayerMoveEvent) {
        val player = event.player
        val profile = player.profile
        val to = event.to
        val location = to.simulatorLocation ?: return

        if (profile.hasUnlockedLocation(location)) return

        event.setCancelled(cancel = true, yaw = false, pitch = false)

        if (profile.wallet.has(location.price)) {
            ConfirmGUI(location, player).open()
        } else {
            Quick.cannotAfford(player, location.price, profile.wallet)
        }
    }

    class ConfirmGUI(val location: SimulatorLocation, player: Player) :
        ConfirmationGUI(player) {

        override fun getTitle(): String {
            return player.getLangMsg(
                MSG.LOCATION_UNLOCK_MENU_TITLE
            ).text
        }

        override fun getMainItem(): ItemStack {
            return location.getIcon(player)
        }

        override fun onConfirm(event: InventoryClickEvent) {
            val profile = player.profile
            location.buy(profile)
        }
    }
}