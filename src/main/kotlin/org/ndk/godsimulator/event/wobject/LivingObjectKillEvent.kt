package org.ndk.godsimulator.event.wobject

import org.bukkit.OfflinePlayer
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.ndk.godsimulator.wobject.LivingObject

class LivingObjectKillEvent(
    val livingObject: LivingObject,
    val killer: OfflinePlayer
) : Event() {

    val damageMap = livingObject.damageMap

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
}