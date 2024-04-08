package org.ndk.godsimulator.equipable

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.ndk.global.interfaces.Snowflake
import org.ndk.godsimulator.equipable.inventory.EquipableInventory
import org.ndk.godsimulator.equipable.type.EquipableType
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.minecraft.extension.setTag
import org.ndk.minecraft.language.MSGHolder
import java.util.*

open class Equipable<T : EquipableType<T>>(
    val type: T,
    override val id: UUID
) : Snowflake<UUID>, MSGNameHolder {

    override val nameMSG: MSGHolder = type.nameMSG
    override val defaultPhName: String = type.manager.qualifier

    /**
     * Called when the object is equipped
     *
     * Called after the object is added to the inventory and after the event is called
     *
     * Method cannot cancel equipping.
     *
     * @param inventory inventory where the object is equipped
     */
    open fun onEquip(inventory: EquipableInventory<T>) {}

    /**
     * Called when the object is unequipped
     *
     * Called after the object is removed from the inventory and after the event is called
     *
     * Method cannot cancel unequipping.
     *
     * @param inventory inventory where the object is unequipped
     */
    open fun onUnEquip(inventory: EquipableInventory<T>) {}

    override fun getIcon(player: Player): ItemStack {
        return type.getIcon(player)
            .setTag("equipableId", id.toString())
    }

    open fun serialize(): String {
        return "$defaultPhName-${type.id}-$id"
    }
}