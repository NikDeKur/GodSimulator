package org.ndk.godsimulator.quest.goal.type

import org.ndk.godsimulator.quest.goal.abc.GoalPattern
import java.math.BigInteger

abstract class CountGoalType<T : GoalPattern<T>, N : Number> : GoalType<T>() {
    abstract val target: N
}

abstract class CounterGoalType<T : GoalPattern<T>> : CountGoalType<T, Int>()
abstract class BigCounterGoalType<T : GoalPattern<T>> : CountGoalType<T, BigInteger>()