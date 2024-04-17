@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.rpg.profile

import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.GodSimulator.Companion.logger
import org.ndk.godsimulator.GodSimulator.Companion.scheduler
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.rpg.buff.AttachableBuffsListImpl
import org.ndk.godsimulator.rpg.buff.RPGBuff
import org.ndk.godsimulator.rpg.combat.CombatTracker
import org.ndk.godsimulator.rpg.stat.*
import org.ndk.klib.Constants.BIGDEC_100
import org.ndk.klib.bigDecBoundVar
import org.ndk.minecraft.Utils.debug
import org.ndk.minecraft.Utils.runSync
import org.ndk.minecraft.extension.nms
import org.ndk.minecraft.extension.sendEntityAnimation
import org.ndk.minecraft.extension.setHighWalkSpeed
import java.math.BigDecimal
import java.math.BigInteger

class RPGPlayerProfile(val profile: PlayerProfile) {

    var latestHealth: BigDecimal by profile.scopes.accessor.bigDecBoundVar("latestHealth")

    val scopes by profile::scopes

    val player
        get() = profile.player

    val onlinePlayer
        get() = profile.onlinePlayer

    val combatTracker = CombatTracker(this, GodSimulator.Properties.playerCombatDelay)

    val buffs = object : AttachableBuffsListImpl() {
        override fun afterAddBuff(buff: RPGBuff<*>) {
            debug("add: $buff")
            val value = buff.value
            when (val stat = buff.stat) {
                is RPGSpeedStat -> {
                    playerSpeed = stat.merge(playerSpeed, value as Int)
                }
                is RPGHealthStat -> {
                    maxHealth = stat.merge(maxHealth.toBigInteger(), value as BigInteger).toBigDecimal()
                    val added = updateMaxHealth()
                    heal(added)
                }
                is RPGStat.Multiplier -> {
                    val mult = multipliers[stat.id]
                    if (mult != null) {
                        multipliers[stat.id] = mult + (value as Double)
                    } else {
                        multipliers[stat.id] = value as Double
                    }
                }
            }
        }

        override fun afterRemoveBuff(buff: RPGBuff<*>) {
            debug("remove: $buff")
            val value = buff.value
            when (val stat = buff.stat) {
                is RPGSpeedStat -> {
                    playerSpeed = stat.deMerge(playerSpeed, value as Int)
                }
                is RPGHealthStat -> {
                    maxHealth = stat.deMerge(maxHealth.toBigInteger(), value as BigInteger).toBigDecimal()
                    val taken = updateMaxHealth().abs()
                    if (health != maxHealth)
                        unHeal(taken)
                }
                is RPGStat.Multiplier -> {
                    multipliers[stat.id] = getMultiplier(stat.id) - (value as Double)
                }
            }
        }
    }




    fun applyOnPlayer() {
        updateSpeed()
        updateMaxHealth(true)


        val isLoading = GodSimulator.instance.modulesManager.isLoading
        val latest = latestHealth
        if (latest != BigInteger.ZERO) {
            health =
                if (isLoading || latest >= maxHealth)
                    maxHealth
                else
                    latest
        }
    }

    fun unApplyOnPlayer() {
        val player = player
        if (player is Player) {
            player.health = 20.0
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).baseValue = 20.0
            runSync {
                player.setHighWalkSpeed(0.2f)
            }
        }
    }







    /**
     * The private field to allow async-safe access to player the speed value
     *
     * Shouldn't be accessed, use [playerSpeed] instead
     *
     *
     * playerSpeed.get() return this value
     *
     * playerSpeed.set(value) set this value and update the player speed in the bukkit main thread
     */
    private var _speed: Int = 20

    /**
     * Set the speed measured in simulator values
     *
     * The simulator values are greater than minecraft 100 times
     *
     * The default speed-value is 20 (simulator) and 0.2 (minecraft)
     */
    var playerSpeed: Int
        get() = _speed
        set(value) {
            _speed = value
            updateSpeed()
        }

    fun updateSpeed() {
        scheduler.runTask {
            onlinePlayer?.setHighWalkSpeed(playerSpeed / 100f)
        }
    }


    /**
     * The max health of the player
     *
     * It is calculated by the health and healthExtraProcent
     *
     * To update maxHealth use [updateMaxHealth]
     */
    var maxHealth: BigDecimal = BigDecimal.valueOf(20)
        private set

    /**
     * The scaled health of the player
     *
     * It is calculated by the maxHealth and the healthExtraProcent
     *
     * This is actual player health, that is displayed in the game and used to damage
     */
    var health: BigDecimal = BigDecimal.valueOf(20)
        set(value) {
            val v =
                if (value <= BigDecimal.ZERO) BigDecimal.ZERO
                else if (value >= maxHealth) maxHealth
                else value

            field = v
            onlinePlayer?.health = v.toDouble()
        }

    val isFullHealth: Boolean
        get() = health == maxHealth

    /**
     * Recalculate the player maxHealth depending on the health and healthExtraProcent
     *
     * @param heal If true, the player will be healed to the max health
     * @return The amount of the max health that was added. If the return value is negative, the max health was decreased if positive - increased if 0 - not changed.
     */
    fun updateMaxHealth(heal: Boolean = false): BigDecimal {
        val maxHealthBefore = maxHealth

        // This should never happen, but if it does, set the max health to 1 and log the error
        if (maxHealth < BigDecimal.ONE) {
            logger.warning("Player max health is less than 1, setting to 1. Player: (${player.name} | ${player.uniqueId}) health: $health | max health: $maxHealth")
            maxHealth = BigDecimal.ONE
        }

        if (health > maxHealth)
            health = maxHealth

        val player = player
        if (player is Player) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).baseValue = maxHealth.toDouble()

            if (heal)
                player.health = maxHealth.toDouble()
        }

        return maxHealth - maxHealthBefore
    }

    /**
     * Heal the player for the given amount of health
     *
     * Increase the health value, but doesn't change or exceed the max health
     */
    fun heal(value: BigDecimal) {
        health += value
    }

    fun healProcent(procent: Double) {
        val value = maxHealth.divide(BIGDEC_100) * procent.toBigDecimal()
        heal(value)
    }

    /**
     * UnHeal the player for the given amount of health
     *
     * Decrease the health value, but doesn't go below 1
     */
    fun unHeal(value: BigDecimal) {
        health = if (health - value < BigDecimal.ONE) {
            BigDecimal.ONE
        } else {
            health - value
        }
    }

    /**
     * Damage the player for the given amount of damage
     *
     * Doesn't change damage anyway, apply the damage to the player.
     *
     * @param damage The damage to apply
     * @param effect If true, player and everyone around will see the minecraft damage effect
     */
    fun damageRaw(damage: BigDecimal, effect: Boolean = true) {
        val newHealth = health - damage
        health = newHealth

        if (effect) {
            val player = player
            if (player is Player) {
                player.sendEntityAnimation(player.nms, 1)
            }
        }
    }

	/**
	 * Damage the player for the given amount of damage
     *
     * Doesn't increase the damage, apply the damage to the player. Method can decrease or cancel damage depending on the player's stats and damage caused.
     *
     * @param damage The damage to apply
     * @param effect If true, player and everyone around will see the minecraft damage effect
     * @return The actual damage that was applied to the player
	 */
    fun damage(damage: BigDecimal, effect: Boolean = true): BigDecimal {
        damageRaw(damage, effect)
        return damage
    }


    val multipliers = HashMap<String, Double>()

    inline fun getMultiplier(id: String) = multipliers[id] ?: 1.0
    inline fun getMultiplier(stat: RPGStat.Multiplier) = getMultiplier(stat.id)

    fun scaleDamage(damage: BigInteger): BigInteger {
        val mult = getMultiplier(RPGDamageMultiplierStat)
        if (mult == 0.0) return damage
        return (damage.toDouble() * mult).toBigDecimal().toBigInteger()
    }

    fun scaleExp(exp: BigInteger): BigInteger {
        val mult = getMultiplier(RPGExpMultiplierStat)
        if (mult == 0.0) return exp
        return (exp.toDouble() * mult).toBigDecimal().toBigInteger()
    }

    fun scaleCurrency(currencyId: String, coins: BigInteger): BigInteger {
        val mult = getMultiplier(currencyId)
        if (mult == 0.0) return coins
        return (coins.toDouble() * mult).toBigDecimal().toBigInteger()
    }



    fun clear() {
        buffs.clear()
    }

    init {
        profile.globalAccessor.beforeSaveHooks.add {
            latestHealth = health
        }

        // Get inventories to awake them from lazy mode.
        // Doing this here, because usually inventories add buffs.
        profile.globalAccessor.whenLoaded {
            scopes.pets.profile
            scopes.auras.profile
            scopes.bags.profile
        }
    }
}
