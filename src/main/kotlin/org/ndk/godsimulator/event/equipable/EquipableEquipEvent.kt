package org.ndk.godsimulator.event.equipable

import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.ndk.godsimulator.equipable.Equipable
import org.ndk.godsimulator.equipable.inventory.EquipableInventory
import org.ndk.godsimulator.equipable.type.EquipableType

class EquipableEquipEvent<T : EquipableType<T>>(
    val inventory: EquipableInventory<T>,
    val equipable: Equipable<T>
) : Event(), Cancellable {

    val profile = inventory.profile
    val rpgProfile = profile.rpg

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }

    var cancel = false
    override fun isCancelled(): Boolean {
        return cancel
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancel = cancel
    }
}