//package org.ndk.godsimulator.rpg.buff
//
//import org.ndk.godsimulator.rpg.stat.RPGStat
//
//class RPGRangeBuff<T : Comparable<T>>(
//    stat: RPGStat<T>,
//    val min: T,
//    val max: T
//) : RPGBuff<T>(stat, min) {
//
//    override fun beforeAdd(buffsList: BuffsList) {
//        value = stat.range(min, max)
//    }
//}