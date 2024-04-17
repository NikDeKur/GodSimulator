package org.ndk.godsimulator.event.equipable

import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.ndk.godsimulator.economy.Buyable
import org.ndk.godsimulator.profile.PlayerProfile

/**
 * Called before allowing the player to buy the item
 *
 * Called after checking if the player has enough money (not taken yet) and if the item is buyable
 *
 * @param profile the player profile
 * @param item the the item
 * @param silent if the player should be notified about the purchase
 */
class EquipablePreBoughtEvent(
    val profile: PlayerProfile,
    val item: Buyable,
    val silent: Boolean,
) : Event(), Cancellable {

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