package org.ndk.godsimulator.quest.goal.impl

import org.ndk.godsimulator.extension.getLangMsg
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.quest.ProfileQuest
import org.ndk.godsimulator.quest.goal.abc.GoalCountPattern

abstract class CountGoal<T : GoalCountPattern<T, N>, N : Number>(quest: ProfileQuest, pattern: T) : Goal<T>(quest, pattern) {

    abstract var progress: N

    abstract fun increment(amount: N)

    override fun getReadableProgress(): String {
        return if (isCompleted)
            profile.getLangMsg(MSG.QUEST_GOAL_COMPLETED).text
        else
            profile.getLangMsg(MSG.QUEST_GOAL_PROGRESS, "progress" to progress, "target" to pattern.target).text
    }
}