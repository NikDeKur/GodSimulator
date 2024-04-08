package org.ndk.godsimulator.quest

import org.bukkit.entity.Player
import org.ndk.global.interfaces.Snowflake
import org.ndk.godsimulator.language.MSGDescriptionHolder
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.godsimulator.quest.manager.QuestsManager
import org.ndk.godsimulator.quest.objective.Objective
import org.ndk.minecraft.language.MSGHolder

/**
 * Represents a quest that the player can complete.
 *
 *
 */
data class Quest(
    override val id: String,
    override val nameMSG: MSGHolder,
    override val descriptionMSG: MSGHolder
) : Snowflake<String>, MSGNameHolder, MSGDescriptionHolder {

    override val defaultPhName: String = "quest"

    val objectives = ArrayList<Objective>()

    fun addObjective(objective: Objective) {
        objectives.add(objective)
    }


    fun create(manager: QuestsManager, player: Player): PlayerQuest {
        val playerQuest = PlayerQuest(manager, this, player)
        objectives.forEach {
            playerQuest.addObjective(it.clone())
        }
        return playerQuest
    }
}