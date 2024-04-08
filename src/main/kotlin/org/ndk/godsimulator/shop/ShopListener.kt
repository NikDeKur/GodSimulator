package org.ndk.godsimulator.shop

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.ndk.godsimulator.GodSimulator.Companion.shopManager

class ShopListener : Listener {

    @EventHandler
    fun onShopOpen(event: PlayerInteractEntityEvent) {
        val entity = event.rightClicked
        if (!shopManager.isShopEntity(entity.uniqueId)) return
        event.isCancelled = true
        ShopMainGUI(event.player).open()
    }
}