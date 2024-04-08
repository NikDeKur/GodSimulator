package org.ndk.godsimulator.listener

import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerGameModeChangeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.ndk.godsimulator.GodSimulator.Companion.scheduler
import org.ndk.minecraft.Utils.debug
import org.ndk.minecraft.extension.cancel
import org.ndk.minecraft.modules.PluginModule

object DoubleJumpManager : Listener, PluginModule {
    override val id: String = "DoubleJumpManager"

    /**
     * Make player double jump
     *
     * Do not do any checks, just make player double jump
     *
     * @param player The player to double jump
     */
    fun doubleJump(player: Player) {
        debug("fire double jump")
        scheduler.runTask {
            player.velocity = player.eyeLocation.direction.multiply(1.2)
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.player.allowFlight = true
    }

    @EventHandler
    fun onPlayerGamemodeChange(event: PlayerGameModeChangeEvent) {
        scheduler.runTask {
            event.player.allowFlight = true
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        event.player.allowFlight = false
    }

    @EventHandler
    fun onDoubleJump(event: PlayerToggleFlightEvent) {
        if (event.player.gameMode == GameMode.CREATIVE) return
        event.cancel()
        val player = event.player
        doubleJump(player)
    }
}