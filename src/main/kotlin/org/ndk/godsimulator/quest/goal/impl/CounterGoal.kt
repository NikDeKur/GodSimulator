package org.ndk.godsimulator.quest.goal.impl

import org.ndk.godsimulator.quest.ProfileQuest
import org.ndk.godsimulator.quest.goal.abc.GoalCountPattern

open class CounterGoal<T : GoalCountPattern<T, Int>>(quest: ProfileQuest, pattern: T) : CountGoal<T, Int>(quest, pattern) {

    override var progress = 0
    override fun increment(amount: Int) {
        if (isCompleted) return
        if (progress + amount >= pattern.target) {
            progress = pattern.target
            complete()
        }
        progress += amount
    }
}