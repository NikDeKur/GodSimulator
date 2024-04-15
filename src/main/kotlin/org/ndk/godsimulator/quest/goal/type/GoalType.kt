package org.ndk.godsimulator.quest.goal.type

import org.ndk.global.interfaces.Snowflake
import org.ndk.godsimulator.quest.goal.abc.GoalPattern

abstract class GoalType<T : GoalPattern<T>> : Snowflake<String> {
    abstract fun read(string: String): T
    abstract fun write(pattern: T): String


    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as GoalType<*>

        return id == other.id
    }

    override fun toString(): String {
        return "GoalType{id='$id'}"
    }
}