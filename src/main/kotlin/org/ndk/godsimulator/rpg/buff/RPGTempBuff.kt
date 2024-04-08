package org.ndk.godsimulator.rpg.buff

import org.ndk.godsimulator.GodSimulator.Companion.scheduler
import org.ndk.godsimulator.rpg.stat.RPGStat

open class RPGTempBuff<T : Comparable<T>>(
    type: RPGStat<T>,
    value: T,
    val durationTicks: Long
) : RPGBuff<T>(type, value) {

    override fun beforeAdd(buffsList: BuffsList) {
        scheduler.runTaskLater(durationTicks) {
            buffsList.removeBuff(this)
        }
    }
}