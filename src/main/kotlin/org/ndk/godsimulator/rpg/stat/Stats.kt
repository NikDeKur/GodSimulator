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


// ----------------------------
// REGENERATION
// ----------------------------
object RPGRegenStat : RPGStat.BigInteger() {
    override val id: String = "regeneration"
    override val nameMSG: MSG = MSG.RPG_STAT_NAME_REGENERATION
    override val nameBuffMSG: MSG = MSG.RPG_STAT_NAME_BUFF_REGENERATION
}

object RPGRegenMultiplierStat : RPGStat.Double() {
    override val id: String = "regeneration_multiplier"
    override val nameMSG: MSG = MSG.RPG_STAT_NAME_REGENERATION_MULTIPLIER
    override val nameBuffMSG: MSG = MSG.RPG_STAT_NAME_BUFF_REGENERATION_MULTIPLIER
}


// ----------------------------
// DAMAGE
// ----------------------------
object RPGDamageMultiplierStat : RPGStat.Double() {
    override val id: String = "damage_multiplier"
    override val nameMSG: MSG = MSG.RPG_STAT_NAME_DAMAGE_MULTIPLIER
    override val nameBuffMSG: MSG = MSG.RPG_STAT_NAME_BUFF_DAMAGE_MULTIPLIER
}


// ----------------------------
// EXPERIENCE
// ----------------------------
object RPGExpMultiplierStat : RPGStat.Double() {
    override val id: String = "xp_multiplier"
    override val nameMSG: MSG = MSG.RPG_STAT_NAME_EXP_MULTIPLIER
    override val nameBuffMSG: MSG = MSG.RPG_STAT_NAME_BUFF_EXP_MULTIPLIER
}


// ----------------------------
// Coins
// ----------------------------
object RPGCoinsMultiplierStat : RPGStat.Double() {
    override val id: String = "coins_multiplier"
    override val nameMSG: MSG = MSG.RPG_STAT_NAME_COINS_MULTIPLIER
    override val nameBuffMSG: MSG = MSG.RPG_STAT_NAME_BUFF_COINS_MULTIPLIER
}


// ----------------------------
// Souls
// ----------------------------
object RPGSoulsMultiplierStat : RPGStat.Double() {
    override val id: String = "souls_multiplier"
    override val nameMSG: MSG = MSG.RPG_STAT_NAME_SOULS_MULTIPLIER
    override val nameBuffMSG: MSG = MSG.RPG_STAT_NAME_BUFF_SOULS_MULTIPLIER
}