package org.ndk.godsimulator.quest.goal.abc

interface GoalCountPattern<T : GoalPattern<T>, N : Number> : GoalPattern<T> {

    val target: N
}