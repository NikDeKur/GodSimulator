package org.ndk.godsimulator.event.wobject

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.ndk.godsimulator.wobject.LivingObject
import java.math.BigInteger

class LivingObjectDamageEvent(
    val livingObject: LivingObject,
    val damager: Player,
    val damage: BigInteger
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