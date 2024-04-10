package org.ndk.godsimulator.rpg

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.ndk.godsimulator.equipable.BuffsEquipable
import org.ndk.godsimulator.event.equipable.EquipableUnEquipEvent
import org.ndk.godsimulator.rpg.stat.RPGHealthStat

class RPGListener : Listener {

    /**
     * Control un equipping items, that change max health
     *
     * Event is cancelled if a player has not enough health to decrease his max health
     */
    @EventHandler
    fun onMaxHealthItemUnEquip(event: EquipableUnEquipEvent<*>) {
        val rpg = event.rpgProfile
        val item = event.equipable
        if (item !is BuffsEquipable<*>) return
        val itemHealthBuff = item.equipBuffs.getBuffValue(RPGHealthStat)

        val maxHealthBonus = itemHealthBuff.toBigDecimal() - rpg.maxHealth

        if (maxHealthBonus >= rpg.health) {
            event.isCancelled = true
        }

    }
}