@file:Suppress("unused")

/**
 * All classes in this file are used to define the RPG stats and their buffs.
 * Their will be loaded and registered automatically by RPGManager and JavaTools.findClasses()
 */

package org.ndk.godsimulator.rpg.stat

import org.ndk.godsimulator.language.MSG

/**
 * Uses simulator values speed-value.
 *
 * The simulator values are greater than minecraft 100 times
 *
 * The default speed-value is 20 (simulator) and 0.2 (minecraft)
 *
 * @see [RPGProfile.speed]
 */
object RPGSpeedStat : RPGStat.Int() {
    override val id: String = "speed"
    override val nameMSG: MSG = MSG.RPG_STAT_NAME_SPEED_BONUS
    override val nameBuffMSG: MSG = MSG.RPG_STAT_NAME_BUFF_SPEED_BONUS
}


// ----------------------------
// HEALTH
// ----------------------------

object RPGHealthStat : RPGStat.BigInteger() {
    override val id: String = "health"
    override val nameMSG: MSG = MSG.RPG_STAT_NAME_HEALTH
    override val nameBuffMSG: MSG = MSG.RPG_STAT_NAME_BUFF_HEALTH
}

object RPGHealthExtraProcentStat : RPGStat.BigInteger() {
    override val id: String = "health_procent"
    override val nameMSG: MSG = MSG.RPG_STAT_NAME_HEALTH_PROCENT
    override val nameBuffMSG: MSG = MSG.RPG_STAT_NAME_BUFF_HEALTH_PROCENT
}


// ----------------------------
// REGENERATION
// ----------------------------
object RPGRegenStat : RPGStat.BigInteger() {
    override val id: String = "regeneration"
    override val nameMSG: MSG = MSG.RPG_STAT_NAME_REGENERATION
    override val nameBuffMSG: MSG = MSG.RPG_STAT_NAME_BUFF_REGENERATION
}

object RPGRegenExtraProcentStat : RPGStat.BigInteger() {
    override val id: String = "regeneration_procent"
    override val nameMSG: MSG = MSG.RPG_STAT_NAME_REGENERATION_PROCENT
    override val nameBuffMSG: MSG = MSG.RPG_STAT_NAME_BUFF_REGENERATION_PROCENT
}


// ----------------------------
// DAMAGE
// ----------------------------
object RPGDamageExtraProcentStat : RPGStat.BigInteger() {
    override val id: String = "damage_procent"
    override val nameMSG: MSG = MSG.RPG_STAT_NAME_DAMAGE_PROCENT
    override val nameBuffMSG: MSG = MSG.RPG_STAT_NAME_BUFF_DAMAGE_PROCENT
}


// ----------------------------
// EXPERIENCE
// ----------------------------
object RPGExpExtraProcentStat : RPGStat.BigInteger() {
    override val id: String = "exp"
    override val nameMSG: MSG = MSG.RPG_STAT_NAME_EXP_PROCENT
    override val nameBuffMSG: MSG = MSG.RPG_STAT_NAME_BUFF_EXP_PROCENT
}


// ----------------------------
// BAG FILL
// ----------------------------
object RPGBagFillExtraProcentStat : RPGStat.BigInteger() {
    override val id: String = "bag_fill"
    override val nameMSG: MSG = MSG.RPG_STAT_NAME_BAG_FILL_PROCENT
    override val nameBuffMSG: MSG = MSG.RPG_STAT_NAME_BUFF_BAG_FILL_PROCENT
}