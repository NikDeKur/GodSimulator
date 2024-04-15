package org.ndk.godsimulator.quest.goal.impl

import org.ndk.godsimulator.quest.ProfileQuest
import org.ndk.godsimulator.quest.goal.abc.GoalCountPattern
import java.math.BigInteger

open class BigCounterGoal<T : GoalCountPattern<T, BigInteger>>(quest: ProfileQuest, pattern: T) : CountGoal<T, BigInteger>(quest, pattern) {

    override var progress: BigInteger = BigInteger.ZERO
    override fun increment(amount: BigInteger) {
        if (isCompleted) return
        if (progress + amount >= pattern.target) {
            progress = pattern.target
            complete()
        }
        progress += amount
    }
}