package org.ndk.godsimulator.quest.goal

import org.ndk.godsimulator.quest.objective.Objective

/**
 * Represents a goal that the player must achieve.
 *
 * Example: Kill 10 zombies.
 */
abstract class Goal(val objective: Objective) {

    val player = objective.player

    abstract fun isCompl(): Boolean
    abstract fun getProgress(): String

    abstract fun clone(): Goal
}