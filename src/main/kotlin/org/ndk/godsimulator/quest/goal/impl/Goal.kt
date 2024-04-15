package org.ndk.godsimulator.quest.goal.impl

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
    open fun complete() { isCompleted = true }

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
    open fun serialize(): String {
        return isCompleted.toString()
    }

    open fun deserialize(data: String) {
        isCompleted = Tools.parseBooleanOrNull(data) ?: false
    }
}