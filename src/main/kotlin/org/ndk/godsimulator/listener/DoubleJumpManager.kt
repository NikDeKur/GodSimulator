package org.ndk.godsimulator.listener

import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerGameModeChangeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.util.Vector
import org.ndk.godsimulator.GodSimulator.Companion.scheduler
import org.ndk.godsimulator.event.profile.ProfileStaminaChangeEvent
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profile
import org.ndk.minecraft.extension.cancel
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.movement.OptiPlayerMoveEvent
import java.util.*

object DoubleJumpManager : Listener, PluginModule {
    override val id: String = "DoubleJumpManager"

    fun updateBukkitState(player: Player) {
        player.allowFlight = !(disallowJumped.contains(player.uniqueId) || disallowStamina.contains(player.uniqueId))
    }

    val disallowStamina = HashSet<UUID>()
    fun allowStamina(player: Player) {
        val profile = player.profile
        if (profile.stamina < 20) return
        disallowStamina.remove(player.uniqueId)
        updateBukkitState(player)
    }

    fun disallowStamina(player: Player) {
        disallowStamina.add(player.uniqueId)
        updateBukkitState(player)
    }

    val disallowJumped = HashSet<UUID>()
    fun allowJumped(player: Player) {
        if (!player.isOnGround) return
        disallowJumped.remove(player.uniqueId)
        updateBukkitState(player)
    }

    fun disallowJumped(player: Player) {
        disallowJumped.add(player.uniqueId)
        updateBukkitState(player)
    }


    /**
     * Make player double jump
     *
     * Takes 20 players' stamina
     *
     * @param player The player to double jump
     * @return True if player double jumped, false if player can't double jump
     */
    fun doubleJump(player: Player): Boolean {
        if (disallowJumped.contains(player.uniqueId)) return false
        if (disallowStamina.contains(player.uniqueId)) return false
        if (!player.profile.takeStamina(20)) return false

        val direction = player.eyeLocation.direction
        val x = direction.x
        val z = direction.z
        val y = 0.3
        val vector = Vector(x, y, z)

        player.velocity = player.velocity.add(vector)

        disallowJumped(player)
        return true
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        allowJumped(event.player)
        allowStamina(event.player)
    }

    @EventHandler
    fun onPlayerGamemodeChange(event: PlayerGameModeChangeEvent) {
        scheduler.runTask {
            allowJumped(event.player)
            allowStamina(event.player)
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        disallowJumped(event.player)
        disallowStamina(event.player)
    }



    @EventHandler
    fun onOptiMove(event: OptiPlayerMoveEvent) {
        val player = event.player
        allowJumped(player)
    }


    @EventHandler
    fun onStaminaChange(event: ProfileStaminaChangeEvent) {
        val player = event.profile.onlinePlayer ?: return
        if (event.newStamina < 20) {
            disallowStamina(player)
        } else {
            allowStamina(player)
        }
    }


    @EventHandler
    fun onDoubleJump(event: PlayerToggleFlightEvent) {
        val player = event.player
        if (player.gameMode in bypassGamemodes) return
        event.cancel()

        // Bukkit may don't relly like, that we're using this event in survival,
        // so wait for a final result and do our things
        scheduler.runTask {
            doubleJump(player)
        }
    }

    val bypassGamemodes = arrayOf(GameMode.CREATIVE, GameMode.SPECTATOR)
}