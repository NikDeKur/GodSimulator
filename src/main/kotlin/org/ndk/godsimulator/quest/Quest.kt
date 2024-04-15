package org.ndk.godsimulator.quest

import org.ndk.global.interfaces.Snowflake
import org.ndk.godsimulator.language.MSGDescriptionHolder
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.godsimulator.quest.goal.abc.GoalPattern
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


    val goals = ArrayList<GoalPattern<*>>()
    fun addGoal(pattern: GoalPattern<*>) {
        goals.add(pattern)
    }

}