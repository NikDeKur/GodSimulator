package org.ndk.godsimulator.listener

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityCombustEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.ndk.godsimulator.database.Database.Companion.accessor
import org.ndk.godsimulator.world.WorldsManager.Companion.data
import org.ndk.minecraft.Utils.debug
import org.ndk.minecraft.extension.cancel
import org.ndk.minecraft.movement.OptiPlayerMoveEvent


class GlobalEventListener : Listener {

    @EventHandler
    fun onFoodLevelChange(event: FoodLevelChangeEvent) {
        event.foodLevel = 20
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        // val player = event.entity as? Player ?: return
        if (event.cause == EntityDamageEvent.DamageCause.FALL) {
            event.cancel()
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        val attacker = event.damager
        val victim = event.entity
        if (attacker is Player && victim is Player) {
            event.cancel()
        }
    }

    @EventHandler
    fun onMoveWhileGoodSelection(event: OptiPlayerMoveEvent) {
        val player = event.player
        val profile = player.accessor.profile
        if (profile.forceSelectGod) {
            event.cancel()
        }
    }

    @EventHandler
    fun onInvulnerableHit(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        if (entity.isInvulnerable)
            event.cancel()
    }

    @EventHandler
    fun onWorldChange(event: PlayerChangedWorldEvent) {
        debug("PlayerChangedWorldEvent")
        val player = event.player
        val world = player.world.data
        world.objectsManager.showForPlayer(player)
    }


    @EventHandler
    fun entityFire(event: EntityCombustEvent) {
        event.cancel()
    }
}