package org.ndk.godsimulator.quest

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonSerializer
import org.ndk.global.interfaces.Snowflake
import org.ndk.global.map.list.ListsHashMap
import org.ndk.godsimulator.language.MSGDescriptionHolder
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.profile.ProfileQuests
import org.ndk.godsimulator.quest.goal.abc.GoalPattern
import org.ndk.godsimulator.quest.goal.impl.Goal
import org.ndk.godsimulator.quest.goal.type.GoalType
import org.ndk.godsimulator.reward.Reward
import org.ndk.minecraft.language.MSGHolder
import java.util.*

open class ProfileQuest(
    val quests: ProfileQuests,
    override val id: UUID, // questId
    val profile: PlayerProfile,
    override val nameMSG: MSGHolder,
    override val descriptionMSG: MSGHolder,
    val reward: Reward,
) : Snowflake<UUID>, MSGNameHolder, MSGDescriptionHolder {

    override val defaultPhName: String = "quest"

    val goals = ListsHashMap<GoalType<*>, Goal<*>>()
    val uncompletedGoals = HashSet<Goal<*>>()

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
        if (!goal.isCompleted) {
            quests.addGoalListener(goal)
            uncompletedGoals.add(goal)
        }
    }


    open fun onGoalComplete(goal: Goal<*>) {
        uncompletedGoals.remove(goal)
        if (uncompletedGoals.isEmpty()) {
            complete()
        }
    }


    open fun complete() {
        quests.removeQuest(id)
        profile.reward(reward)
        quests.data.remove(id.toString())
    }



    override fun toString(): String {
        val goals = goals.values.flatten()
        return "ProfileQuest(id=$id, nameMSG=${nameMSG.id}, descriptionMSG=${descriptionMSG.id}, goals=$goals)"
    }


    companion object {
        val TypeAdapter = JsonSerializer<ProfileQuest> { quest, _, context ->
            val obj = JsonObject()
            obj.addProperty("name", quest.nameMSG.id)
            obj.addProperty("description", quest.descriptionMSG.id)
            val secGoals = JsonObject()
            quest.goals.forEach { (type, goals) ->
                val goalsTypeGroup = JsonArray()
                goals.forEach {
                    val goalJson = context.serialize(it)
                    goalsTypeGroup.add(goalJson)
                }
                secGoals.add(type.id, goalsTypeGroup)
            }
            obj.add("goals", secGoals)
            obj
        }
    }
}