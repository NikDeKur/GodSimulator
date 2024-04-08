package org.ndk.godsimulator.skill

import org.bukkit.Location
import org.bukkit.entity.Player
import org.ndk.godsimulator.extension.getNearbyBuildings
import org.ndk.godsimulator.extension.getNearbyEntities
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profile
import org.ndk.minecraft.extension.getNearbyPlayers
import java.math.BigInteger
import java.util.*


abstract class SimulatorSkillExecution(val attacker: Player) : SkillExecution(attacker) {

    open val buildingsDamageRadius: Double = 0.0
    open val entitiesDamageRadius: Double = 0.001
    open val playersDamageRadius: Double = 0.0

    open val damagePlayers: Boolean = true
    open val damageBuildings: Boolean = true
    open val damageEntities: Boolean = true

    open val repeatDamage: Boolean = false

    abstract val damage: BigInteger


    open val damaged = HashSet<UUID>()

    fun registerDamage(
        location: Location,
    ): Boolean {
        var result = false

        val damage = damage
        val finalDamage = attacker.profile.rpg.scaleDamage(damage)

        if (damageBuildings) {
            val buildings = location.getNearbyBuildings(attacker, buildingsDamageRadius)
            buildings.forEach {
                if (!repeatDamage && damaged.contains(it.id)) return@forEach
                damaged.add(it.id)
                result = true
                it.damage(attacker, finalDamage)
            }
        }

        if (damageEntities) {
            val entities = location.getNearbyEntities(attacker, entitiesDamageRadius)
            entities.forEach {
                if (!repeatDamage && damaged.contains(it.id)) return@forEach
                damaged.add(it.id)
                result = true
                it.damage(attacker, finalDamage)
            }
        }

        if (damagePlayers) {
            val players = location.getNearbyPlayers(playersDamageRadius)
            players.forEach {
                if (!repeatDamage && damaged.contains(it.uniqueId)) return@forEach
                damaged.add(it.uniqueId)
                result = true
				it.profile.rpg.damage(finalDamage.toBigDecimal())
            }
        }
        return result
    }

}