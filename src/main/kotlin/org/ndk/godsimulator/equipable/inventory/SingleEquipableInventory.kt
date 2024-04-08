package org.ndk.godsimulator.equipable.inventory

import org.ndk.godsimulator.equipable.Equipable
import org.ndk.godsimulator.equipable.type.EquipableType
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.klib.firstValueOrNull

abstract class SingleEquipableInventory<T : EquipableType<T>>(profile: PlayerProfile) : EquipableInventory<T>(profile) {
    override val equipLimit = 1

    var item: Equipable<T>? = null

    override fun equip(obj: Equipable<T>): Boolean {
        val old = equipped.firstValueOrNull()

        // If there is already an equipped item, try to unequip it
        if (old != null) {
            if (!unEquip(old)) return false
        }

        val equipped = super.equip(obj)

        // If the item was not equipped, try to equip the old item back
        if (!equipped) {
            if (old != null) {
                super.equip(old)
            }
        } else {
            item = obj
        }

        return equipped
    }

    override fun unEquip(obj: Equipable<T>): Boolean {
        val unEquipped = super.unEquip(obj)
        if (unEquipped)
            item = null
        return unEquipped
    }
}