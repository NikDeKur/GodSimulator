package org.ndk.godsimulator.skill.cast

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profile

class SkillCastListener : Listener {

    @EventHandler
    fun onHotbatSlotSwap(event: PlayerItemHeldEvent) {
        val player = event.player
        if (!player.profile.passSkillCast) {
            player.inventory.heldItemSlot = 7
            CastManager.cast(player, event.newSlot)
        }
    }
}