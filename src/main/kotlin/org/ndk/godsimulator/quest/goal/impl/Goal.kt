package org.ndk.godsimulator.quest.goal.impl

import com.google.gson.JsonObject
import com.google.gson.JsonSerializer
import org.ndk.global.tools.Tools
import org.ndk.godsimulator.extension.getLangMsg
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.quest.ProfileQuest
import org.ndk.godsimulator.quest.goal.abc.GoalPattern

/**
 * Base class for all profile quest goals.
 *
 * Subclasses should store the progress of the goal and implement the logic of its completion.
 *
 * @see GoalPattern
 * @see ProfileQuest
 */
open class Goal<T : GoalPattern<T>> (val quest: ProfileQuest, val pattern: T) {

    val profile by quest::profile

    open var isCompleted = false
    open fun complete() {
        isCompleted = true

    }

    /**
     * Get string with human-readable progress.
     *
     * @return Human-readable progress.
     */
    open fun getReadableProgress(): String {
        return profile.getLangMsg(
            if (isCompleted) {
                MSG.QUEST_GOAL_COMPLETED
            } else {
                MSG.QUEST_GOAL_UNCOMPLETED
            }
        ).text
    }

    /**
     * Serialize goal progress.
     *
     * @return Serialized progress.
     */
    open fun serializeProgress(): String {
        return isCompleted.toString()
    }

    open fun deserializeProgress(data: String) {
        isCompleted = Tools.parseBooleanOrNull(data) ?: false
    }

    override fun toString(): String {
        return "Goal(pattern=$pattern, progress=${serializeProgress()})"
    }

    companion object {
        val TypeAdapter = JsonSerializer<Goal<*>> { goal, _, _ ->
                val obj = JsonObject()
                obj.addProperty("data", goal.pattern.serializeData())
                obj.addProperty("progress", goal.serializeProgress())
                obj
            }
    }
}