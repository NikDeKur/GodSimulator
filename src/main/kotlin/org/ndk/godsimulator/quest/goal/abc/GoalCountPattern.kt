package org.ndk.godsimulator.quest.goal.abc

abstract class GoalCountPattern<T : GoalPattern<T>, N : Number> : GoalPattern<T>() {

    abstract val target: N
}