package org.ndk.godsimulator.equipable

import org.ndk.godsimulator.equipable.inventory.EquipableInventory
import org.ndk.godsimulator.equipable.type.EquipableType
import org.ndk.godsimulator.rpg.buff.ImaginaryBuffsList
import java.util.*

class BuffsEquipable<T : EquipableType<T>>(
    type: T,
    id: UUID,
    val equipBuffs: ImaginaryBuffsList,
) : Equipable<T>(type, id) {

    /**
     * String id that will be used to attach and detach buffs in BuffsList
     *
     * It should be unique and human-readable for each equipable object
     */
    val buffsId: String = "$defaultPhName-${type.id}-$id"

    override fun onEquip(inventory: EquipableInventory<T>) {
        inventory.profile.rpg.buffs.attach(buffsId, equipBuffs)
    }

    override fun onUnEquip(inventory: EquipableInventory<T>) {
        inventory.profile.rpg.buffs.detach(buffsId)
    }
}