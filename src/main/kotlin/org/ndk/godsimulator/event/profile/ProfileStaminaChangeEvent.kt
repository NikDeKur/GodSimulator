package org.ndk.godsimulator.event.profile

import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.ndk.godsimulator.profile.PlayerProfile

class ProfileStaminaChangeEvent(
    profile: PlayerProfile,
    val oldStamina: Int,
    var newStamina: Int
) : ProfileEvent(profile), Cancellable {


    override fun getHandlers() = HANDLERS
    companion object {
        private val HANDLERS = HandlerList()
        @JvmStatic
        fun getHandlerList() = HANDLERS
    }

    var cancel = false
    override fun isCancelled(): Boolean {
        return cancel
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancel = cancel
    }
}