package org.ndk.godsimulator.quest.listening

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.ndk.godsimulator.event.wobject.LivingObjectDamageEvent
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profileAsync
import org.ndk.godsimulator.quest.goal.abc.GoalPattern
import org.ndk.godsimulator.quest.goal.impl.BigCounterGoal
import org.ndk.godsimulator.quest.goal.impl.Goal
import org.ndk.godsimulator.quest.goal.type.GoalType
import org.ndk.godsimulator.quest.goal.type.GoalTypes
import org.ndk.minecraft.movement.OptiPlayerMoveEvent

object GoalsListener : Listener {
    fun <T : GoalPattern<T>> forEachGoal(player: Player, type: GoalType<T>, action: (Goal<T>) -> Unit) {
        player.profileAsync.thenAccept { profile ->
            profile.quests.getGoals(type).forEach(action)
        }
    }


    @EventHandler
    fun goToLocation(event: OptiPlayerMoveEvent) {
        val player = event.player
        val to = event.to
        forEachGoal(player, GoalTypes.GO_TO_LOCATION) {
            if (it.pattern.checkPass(to)) {
                it.complete()
            }
        }
    }

    @EventHandler
    fun dealDamage(event: LivingObjectDamageEvent) {
        val player = event.damager
        forEachGoal(player, GoalTypes.DEAL_DAMAGE) {
            if (it !is BigCounterGoal) return@forEachGoal
            it.increment(event.damage)
        }
    }
}