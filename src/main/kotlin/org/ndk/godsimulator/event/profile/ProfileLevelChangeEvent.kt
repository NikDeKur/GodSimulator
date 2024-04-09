package org.ndk.godsimulator.event.profile

import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.ndk.godsimulator.profile.PlayerProfile

class ProfileLevelChangeEvent(
    profile: PlayerProfile,
    val oldLevel: Int,
    var newLevel: Int
) : ProfileEvent(profile), Cancellable {

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