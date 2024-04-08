@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.quest.goal

import org.ndk.godsimulator.quest.objective.Objective

abstract class SimpleGoal(objective: Objective) : Goal(objective) {

    var compl = false
    override fun isCompl(): Boolean {
        return compl
    }

    fun setCompleted(completed: Boolean) {
        this.compl = completed
    }

    inline fun complete() {
        setCompleted(true)
    }
}