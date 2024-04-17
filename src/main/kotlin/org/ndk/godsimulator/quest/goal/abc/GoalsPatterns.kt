package org.ndk.godsimulator.quest.goal.abc

import org.bukkit.Location
import org.ndk.godsimulator.quest.goal.type.GoalTypes
import org.ndk.klib.distanceSquared
import java.math.BigInteger

class GoToLocationGoalPattern(
    val x: Double,
    val y: Double,
    val z: Double,
    val radius: Double
) : GoalPattern<GoToLocationGoalPattern>() {
    val radiusSquared = radius * radius
    override val type = GoalTypes.GO_TO_LOCATION

    fun checkPass(location: Location): Boolean {
        val lX = location.x
        val lY = location.y
        val lZ = location.z
        val distance = distanceSquared(lX, lY, lZ, x, y, z)
        return distance <= radiusSquared
    }
}

class DealDamageGoalPattern(
    override val target: BigInteger
) : GoalCountPattern<DealDamageGoalPattern, BigInteger>() {

    override val type = GoalTypes.DEAL_DAMAGE
}

