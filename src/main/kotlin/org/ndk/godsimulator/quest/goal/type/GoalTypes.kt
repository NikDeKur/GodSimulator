package org.ndk.godsimulator.quest.goal.type

import org.ndk.godsimulator.quest.goal.abc.DealDamageGoalPattern
import org.ndk.godsimulator.quest.goal.abc.GoToLocationGoalPattern
import org.ndk.godsimulator.quest.manager.QuestsManager

object GoalTypes {


    fun register(manager: QuestsManager) {
        manager.addGoalType(GO_TO_LOCATION)
        manager.addGoalType(DEAL_DAMAGE)
    }

    val GO_TO_LOCATION = object : GoalType<GoToLocationGoalPattern>() {
        override val id: String = "go_to_location"
        override fun read(string: String): GoToLocationGoalPattern {
            val parts = string.replace(" ", "").split(",")
            return GoToLocationGoalPattern(
                parts[0].toDouble(),
                parts[1].toDouble(),
                parts[2].toDouble(),
                parts[3].toDouble()
            )
        }
        override fun write(pattern: GoToLocationGoalPattern): String {
            return "${pattern.x}, ${pattern.y}, ${pattern.z}, ${pattern.radius}"
        }
    }


    val DEAL_DAMAGE = object : GoalType<DealDamageGoalPattern>() {
        override val id: String = "deal_damage"
        override fun read(string: String): DealDamageGoalPattern {
            return DealDamageGoalPattern(string.toBigInteger())
        }
        override fun write(pattern: DealDamageGoalPattern): String {
            return pattern.target.toString()
        }
    }
}