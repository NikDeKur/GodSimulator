package org.ndk.godsimulator.skill.cast

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent
import org.ndk.godsimulator.menu.SkillsGUI
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profile
import org.ndk.godsimulator.skill.Skill
import java.util.*

object SkillListener : Listener {

    val bindWait = HashMap<UUID, Skill>()
    fun isWaitingBind(player: Player): Boolean {
        return bindWait.containsKey(player.uniqueId)
    }
    fun getBindWait(player: Player): Skill? {
        return bindWait[player.uniqueId]
    }
    fun waitBind(player: Player, skill: Skill) {
        bindWait[player.uniqueId] = skill
    }

    @EventHandler
    fun onHotbatSlotSwap(event: PlayerItemHeldEvent) {
        val player = event.player
        val newSlot = event.newSlot

        val wait = getBindWait(player)
        if (wait != null) {
            SkillsGUI.bind(player, wait, newSlot)
            bindWait.remove(player.uniqueId)
            player.inventory.heldItemSlot = 7
            return
        }

        if (!player.profile.passSkillCast) {
            player.inventory.heldItemSlot = 7
            CastManager.cast(player, newSlot)
        }
    }
}