package org.ndk.godsimulator.shop

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.ndk.godsimulator.equipable.impl.AurasShopGUI
import org.ndk.godsimulator.equipable.impl.BagsShopGUI

object ShopListener : Listener {

    @EventHandler
    fun onShopOpen(event: PlayerInteractEntityEvent) {
        val entity = event.rightClicked
        event.isCancelled = true
        val shop = ShopManager.getShopByEntity(entity.uniqueId) ?: return
        val type = shop.pattern.id
        when (type) {
            "bags" -> BagsShopGUI(event.player)
            "auras" -> AurasShopGUI(event.player)
            else -> null
        }?.open()
    }
}