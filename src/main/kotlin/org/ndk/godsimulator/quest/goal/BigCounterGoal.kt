package org.ndk.godsimulator.quest.goal

import org.ndk.godsimulator.quest.objective.Objective
import java.math.BigInteger

abstract class BigCounterGoal(objective: Objective) : Goal(objective) {

    abstract val target: BigInteger
    var progress: BigInteger = BigInteger.ZERO

    fun increment(amount: BigInteger = BigInteger.ONE) {
        progress += amount
    }

    override fun isCompl(): Boolean {
        return progress >= target
    }

    override fun getProgress(): String {
        return ""
    }
}