package org.ndk.godsimulator.rpg.combat

import org.ndk.godsimulator.rpg.profile.RPGPlayerProfile
import org.ndk.godsimulator.wobject.LivingObject
import java.util.*

/**
 * Represents a combat tracker that tracks combat time of living objects.
 *
 * This class is used to track the combat time of living objects.
 * It is used to determine if a player is in combat or not.
 *
 * @param rpg Player RPG profile
 * @param combatDelay Combat Delay in ms
 */
class CombatTracker(val rpg: RPGPlayerProfile, val combatDelay: Long) {

    val combatTimeMap = LinkedHashMap<UUID, Long>()

    var combatStartTime = 0L
    val isInCombat: Boolean
        get() = (combatStartTime + combatDelay) > System.currentTimeMillis()


    fun enterCombat(obj: LivingObject) {
        val time = System.currentTimeMillis()
        combatTimeMap[obj.id] = time
        combatStartTime = time
    }

    fun leaveCombat(obj: LivingObject) {
        combatTimeMap.remove(obj.id)
    }
}