package org.ndk.godsimulator.quest

import org.bukkit.entity.Player
import org.ndk.godsimulator.quest.manager.QuestsManager
import org.ndk.godsimulator.quest.objective.Objective

class PlayerQuest(
    val manager: QuestsManager,
    val quest: Quest,
    val player: Player
) {

    val objectives = ArrayList<Objective>()

    fun addObjective(objective: Objective) {
        objectives.add(objective)
    }
}