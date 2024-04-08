package org.ndk.godsimulator.equipable.type

import org.ndk.godsimulator.buying.Buyable
import org.ndk.godsimulator.profile.PlayerProfile

interface BuyableEquipableType<T>
    : EquipableType<T>, Buyable where T : BuyableEquipableType<T>, T : Buyable {

    override fun afterEvent(profile: PlayerProfile, silent: Boolean): Boolean {
        val inventory = manager.getInventory(profile)
        val equipable = this.newEquipable()
        val add = inventory.add(equipable)
        if (add) {
            return inventory.equip(equipable)
        }
        return false
    }
}