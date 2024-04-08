package org.ndk.godsimulator.quest.goals

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.ndk.godsimulator.quest.goal.CounterGoal
import org.ndk.godsimulator.quest.goal.SimpleGoal
import org.ndk.godsimulator.quest.objective.Objective

// Global Minecraft Goals
class KillEntityGoal(objective: Objective, val entity: EntityType, override val target: Int) : CounterGoal(objective) {

    override fun getProgress(): String {
        return ""
    }

    override fun clone(): KillEntityGoal {
        return KillEntityGoal(objective, entity, target)
    }
}

class WalkToLocationGoal(objective: Objective, val location: Location, val radius: Double) : SimpleGoal(objective) {

    val radiusSquared = radius * radius

    override fun getProgress(): String {
        return ""
    }

    override fun clone(): WalkToLocationGoal {
        return WalkToLocationGoal(objective, location, radius)
    }
}
