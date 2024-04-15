package org.ndk.godsimulator.quest

import com.google.gson.stream.JsonWriter
import org.ndk.global.interfaces.Snowflake
import org.ndk.global.map.list.ListsMap
import org.ndk.godsimulator.language.MSGDescriptionHolder
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.profile.ProfileQuests
import org.ndk.godsimulator.quest.goal.abc.GoalPattern
import org.ndk.godsimulator.quest.goal.impl.Goal
import org.ndk.godsimulator.quest.goal.type.GoalType
import org.ndk.klib.listsMapBoundVar
import org.ndk.minecraft.language.MSGHolder
import java.util.*

class ProfileQuest(
    val quests: ProfileQuests,
    override val id: UUID, // questId
    val profile: PlayerProfile,
    override val nameMSG: MSGHolder,
    override val descriptionMSG: MSGHolder
) : Snowflake<UUID>, MSGNameHolder, MSGDescriptionHolder {

    override val defaultPhName: String = "quest"

    val goals: ListsMap<GoalType<*>, Goal<*>> by quests.data.listsMapBoundVar("goals")

    /**
     * Create a goal from a pattern and add it to the quest.
     *
     * @param pattern Goal pattern to create a goal from.
     * @return Created goal.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : GoalPattern<T>> addGoal(pattern: GoalPattern<*>): Goal<T> {
        // Adding cast due to type erasure
        val goal = Goal(this, pattern as T)
        addGoal(goal)
        return goal
    }

    /**
     * Add a goal to the quest.
     *
     * Register the goal in the [goals] map and add a listener to it in [quests].
     *
     * @param goal Goal to add.
     */
    fun addGoal(goal: Goal<*>) {
        goals.add(goal.pattern.type, goal)
        quests.addGoalListener(goal)
    }




    fun serialize(writer: JsonWriter) {
        writer.beginObject()
        writer.name("name").value(nameMSG.id)
        writer.name("description").value(descriptionMSG.id)
        val secGoals = writer.name("goals")
        secGoals.beginObject()
        goals.forEach { (type, goals) ->
            val secPattern = secGoals.name(type.id)
            secPattern.beginArray()
            goals.forEach {
                secPattern.beginObject()
                /**
                 * id: {
                 *   "name": name,
                 *   "description": description,
                 *   "goals": {
                 *     "patternId": [
                 *       {
                 *         "data": "pattern data",
                 *         "progress": "serialize() call"
                 *       }
                 *     ]
                 *   }
                 *
                 */
                secPattern.name("data").value(it.pattern.serialize())
                secPattern.name("progress").value(it.serialize())
                secPattern.endObject()
            }
            secPattern.endArray()
        }

        secGoals.endObject()
        writer.endObject()

    }


    companion object {

//        fun deserialize(profile: PlayerProfile, id: UUID, data: Map<String, Any>): ProfileQuest {
//            val quest = QuestsManager.getQuest(data["quest"] as String)
//            val profileQuest = ProfileQuest(id, profile, quest)
//            quest.goals.forEach { profileQuest.addGoal(it) }
//            return profileQuest
//        }
    }
}