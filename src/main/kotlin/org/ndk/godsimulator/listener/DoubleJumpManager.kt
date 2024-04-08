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
        player.allowFlight = !(disallowJumpsJumped.contains(player.uniqueId) || disallowJumpsStamina.contains(player.uniqueId))
    }

    val disallowJumpsStamina = HashSet<UUID>()
    fun allowJumpStamina(player: Player) {
        val profile = player.profile
        if (profile.stamina < 20) return
        disallowJumpsStamina.remove(player.uniqueId)
        updateBukkitState(player)
    }

    fun disallowJumpStamina(player: Player) {
        disallowJumpsStamina.add(player.uniqueId)
        updateBukkitState(player)
    }

    val disallowJumpsJumped = HashSet<UUID>()
    fun allowJumpJumped(player: Player) {
        if (!player.isOnGround) return
        disallowJumpsJumped.remove(player.uniqueId)
        updateBukkitState(player)
    }

    fun disallowJumpJumped(player: Player) {
        disallowJumpsJumped.add(player.uniqueId)
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
        if (disallowJumpsJumped.contains(player.uniqueId)) return false
        if (disallowJumpsStamina.contains(player.uniqueId)) return false
        if (!player.profile.takeStamina(20)) return false

        val direction = player.eyeLocation.direction
        val x = direction.x
        val z = direction.z
        val y = 0.3
        val vector = Vector(x, y, z)

        player.velocity = player.velocity.add(vector)

        disallowJumpJumped(player)
        return true
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        allowJumpJumped(event.player)
        allowJumpStamina(event.player)
    }

    @EventHandler
    fun onPlayerGamemodeChange(event: PlayerGameModeChangeEvent) {
        scheduler.runTask {
            allowJumpJumped(event.player)
            allowJumpStamina(event.player)
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        disallowJumpJumped(event.player)
        disallowJumpStamina(event.player)
    }



    @EventHandler
    fun onOptiMove(event: OptiPlayerMoveEvent) {
        val player = event.player
        allowJumpJumped(player)
    }


    @EventHandler
    fun onStaminaChange(event: ProfileStaminaChangeEvent) {
        val player = event.profile.onlinePlayer ?: return
        if (event.newStamina < 20) {
            disallowJumpStamina(player)
        } else {
            allowJumpStamina(player)
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