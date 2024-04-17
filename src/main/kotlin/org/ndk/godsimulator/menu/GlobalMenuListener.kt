package org.ndk.godsimulator.menu

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object GlobalMenuListener : Listener {

    @EventHandler
    fun onHandSwap(event: org.bukkit.event.player.PlayerSwapHandItemsEvent) {
        event.isCancelled = true
        GlobalMenu(event.player).open()
    }
}