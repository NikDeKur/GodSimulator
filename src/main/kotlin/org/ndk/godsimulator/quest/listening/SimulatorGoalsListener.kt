package org.ndk.godsimulator.quest.listening

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.ndk.godsimulator.event.wobject.LivingObjectKillEvent
import org.ndk.godsimulator.quest.goals.KillBuildingsGoal
import org.ndk.godsimulator.quest.goals.KillEntitiesGoal
import org.ndk.godsimulator.quest.goals.KillLivingObjectsGoal
import org.ndk.godsimulator.quest.manager.GoalsManager
import org.ndk.godsimulator.wobject.building.Building
import org.ndk.godsimulator.wobject.entity.Entity

class SimulatorGoalsListener(val manager: GoalsManager) : Listener {

    @EventHandler
    fun killLivingObjects(event: LivingObjectKillEvent) {
        val killer = event.killer
        manager.forEachGoal<KillLivingObjectsGoal>(killer) {
            it.increment()
        }
    }

    @EventHandler
    fun killBuildings(event: LivingObjectKillEvent) {
        val obj = event.livingObject
        val killer = event.killer
        if (obj !is Building) return
        val pattern = obj.pattern
        manager.forEachGoal<KillBuildingsGoal>(killer) {
            if (it.pattern != pattern) return@forEachGoal
            it.increment()
        }
    }

    @EventHandler
    fun killEntities(event: LivingObjectKillEvent) {
        val obj = event.livingObject
        val killer = event.killer
        if (obj !is Entity<*>) return
        val pattern = obj.pattern
        manager.forEachGoal<KillEntitiesGoal>(killer) {
            if (it.pattern != pattern) return@forEachGoal
            it.increment()
        }
    }
}