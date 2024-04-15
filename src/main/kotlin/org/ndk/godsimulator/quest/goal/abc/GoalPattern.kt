package org.ndk.godsimulator.quest.goal.abc

import org.ndk.godsimulator.quest.goal.type.GoalType

interface GoalPattern<T : GoalPattern<T>> {

    val type: GoalType<T>

    @Suppress("UNCHECKED_CAST")
    fun serialize(): String {
        return type.write(this as T)
    }
}