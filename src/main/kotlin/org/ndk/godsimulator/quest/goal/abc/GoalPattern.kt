package org.ndk.godsimulator.quest.goal.abc

import org.ndk.godsimulator.quest.goal.type.GoalType

abstract class GoalPattern<T : GoalPattern<T>> {

    abstract val type: GoalType<T>

    @Suppress("UNCHECKED_CAST")
    fun serializeData(): String {
        return type.write(this as T)
    }

    override fun toString(): String {
        return "GoalPattern(type=$type, data=${serializeData()})"
    }
}