package org.ndk.godsimulator.quest.listening

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.ndk.godsimulator.quest.goals.KillEntityGoal
import org.ndk.godsimulator.quest.goals.WalkToLocationGoal
import org.ndk.godsimulator.quest.manager.GoalsManager
import org.ndk.minecraft.movement.OptiPlayerMoveEvent

class GoalsListener(val manager: GoalsManager) : Listener {

    // Kill Entity Goal Listener
    @EventHandler
    fun killEntity(event: EntityDeathEvent) {
        val entity = event.entity
        val killer = entity.killer ?: return
        manager.forEachGoal<KillEntityGoal>(killer) {
            if (it.entity != entity.type) return@forEachGoal
            it.increment()
        }
    }


    // Go to Location Goal Listener
    @EventHandler
    fun goToLocation(event: OptiPlayerMoveEvent) {
        val player = event.player
        val location = player.location
        val to = event.to
        manager.forEachGoal<WalkToLocationGoal>(player) {
            if (it.location.world != location.world) return@forEachGoal
            if (it.location.distanceSquared(to) <= it.radiusSquared) {
                it.complete()
            }
        }
    }
}