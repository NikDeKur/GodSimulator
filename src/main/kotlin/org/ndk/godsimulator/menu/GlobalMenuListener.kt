package org.ndk.godsimulator.menu

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GlobalMenuListener : Listener {

    @EventHandler
    fun onHandSwap(event: org.bukkit.event.player.PlayerSwapHandItemsEvent) {
        event.isCancelled = true
        GlobalMenu(event.player).open()
    }
}