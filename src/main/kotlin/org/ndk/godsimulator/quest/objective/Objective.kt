package org.ndk.godsimulator.quest.objective

import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.language.MSGDescriptionHolder
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.godsimulator.quest.PlayerQuest
import org.ndk.godsimulator.quest.goal.Goal
import java.util.*

class Objective(
    val quest: PlayerQuest,
    override val nameMSG: MSG,
    override val descriptionMSG: MSG
) : MSGNameHolder, MSGDescriptionHolder {

    val player = quest.player

    override val defaultPhName: String = "objective"

    val goals = LinkedList<Goal>()

    fun addGoal(goal: Goal) {
        goals.add(goal)
    }

    fun isCompleted(): Boolean {
        return goals.all { it.isCompl() }
    }

    fun clone(): Objective {
        return Objective(quest, nameMSG, descriptionMSG).apply {
            goals.addAll(goals.map { it.clone() })
        }
    }
}