package org.ndk.godsimulator.quest.goal

import org.ndk.godsimulator.quest.objective.Objective

abstract class CounterGoal(objective: Objective) : Goal(objective) {

    /**
     * The progress of the goal.
     *
     * Default is 0.
     */
    var progress: Int = 0
    abstract val target: Int


    fun increment(amount: Int = 1) {
        progress += amount
    }

    override fun isCompl(): Boolean {
        return progress >= target
    }
}