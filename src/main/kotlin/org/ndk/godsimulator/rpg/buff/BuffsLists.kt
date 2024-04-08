package org.ndk.godsimulator.rpg.buff

import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.rpg.stat.RPGStat
import org.ndk.klib.toComparableResult
import java.util.*

interface BuffsList : Comparable<BuffsList> {

    fun <T : Comparable<T>> getBuffs(id: String): Collection<RPGBuff<T>>
    fun <T : Comparable<T>> getBuffs(type: RPGStat<T>): Collection<RPGBuff<T>>
    fun <T : Comparable<T>> getBuff(type: RPGStat<T>, id: UUID): RPGBuff<*>?
    fun getBuffs(): Map<String, Map<UUID, RPGBuff<*>>>


    fun <T : Comparable<T>> getBuffValueOrNull(type: RPGStat<T>): T?
    fun <T : Comparable<T>> getBuffValue(type: RPGStat<T>): T

    fun <T : Comparable<T>> addBuff(buff: RPGBuff<T>)
    fun <T : Comparable<T>> addBuff(type: RPGStat<T>, value: T)

    fun <T : Comparable<T>> removeBuffs(type: RPGStat<T>): Collection<RPGBuff<T>>
    fun <T : Comparable<T>> removeBuff(buff: RPGBuff<T>)


    fun beforeAddBuff(buff: RPGBuff<*>) {}
    fun afterAddBuff(buff: RPGBuff<*>) {}

    fun beforeRemoveBuff(buff: RPGBuff<*>) {}
    fun afterRemoveBuff(buff: RPGBuff<*>) {}

    fun beforeClear() {}
    fun clear()
    fun afterClear() {}

    fun forEachStat(action: (String) -> Unit)
    fun forEachBuff(action: (RPGBuff<*>) -> Unit)

    /**
     * Compare this BuffsList with other BuffsList by buffs value
     *
     * Take all buffs from this BuffsList and compare them with buffs from other BuffsList
     *
     * If this BuffsList has more buffs with higher value than other BuffsList, return positive number
     *
     * If this BuffsList has more buffs with lower value than other BuffsList, return negative number
     *
     * @param other BuffsList to compare with
     */
    fun compareByBuffsValue(other: BuffsList): Int {
        var res = 0
        forEachStat {
            val stat = GodSimulator.rpgManager.getAnyType(it) ?: return@forEachStat
            val thisBuff = getBuffValueOrNull(stat)
            val otherBuff = other.getBuffValueOrNull(stat)
            if (thisBuff == null || otherBuff == null) {
                return@forEachStat
            }

            if (thisBuff > otherBuff)
                res++
            else if (thisBuff < otherBuff)
                res--
        }

        return res.toComparableResult()
    }

    fun compareByBuffsAmount(other: BuffsList): Int {
        return (getBuffs().size - other.getBuffs().size).toComparableResult()
    }


    /**
     * Compare this BuffsList with other BuffsList
     *
     * By default, use [compareByBuffsValue] to compare
     */
    override fun compareTo(other: BuffsList): Int {
        return compareByBuffsValue(other)
    }
}

interface ImaginaryBuffsList : BuffsList {
    fun addTo(buffsList: BuffsList): List<RPGBuff<*>>
}


data class AttachedBuffsList(val id: String, val buffs: List<RPGBuff<*>>)

interface AttachableBuffsList : BuffsList {
    val attaches: MutableMap<String, AttachedBuffsList>
    fun attach(id: String, buffs: ImaginaryBuffsList)
    fun detach(id: String)
}