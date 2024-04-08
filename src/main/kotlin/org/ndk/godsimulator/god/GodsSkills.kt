@file:Suppress("ClassName")

package org.ndk.godsimulator.god

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.server.v1_12_R1.PacketPlayOutExplosion
import net.minecraft.server.v1_12_R1.Vec3D
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profile
import org.ndk.godsimulator.skill.SimulatorSkillExecution
import org.ndk.minecraft.extension.broadcast
import org.ndk.minecraft.extension.getTargetLocation
import org.ndk.minecraft.extension.strikeLightningEffect
import java.math.BigDecimal
import java.math.BigInteger


val DEFAULT_TRANSPARENT = setOf(
    Material.LONG_GRASS, Material.AIR
)


fun defaultDamageScaling(player: Player, default: BigDecimal): BigInteger {
    val levelMultiplier = player.profile.level / 15.0
    val bonus = default.times(BigDecimal.valueOf(levelMultiplier))
    return default.plus(bonus).toBigInteger()
}

fun Player.getTargetBlock(maxDistance: Int): Block? {
    return getTargetBlock(DEFAULT_TRANSPARENT, maxDistance)
}

class Zeus_ThunderBolt(val player: Player) : SimulatorSkillExecution(player) {

    override val listenBlocks: Boolean = false
    override val listenMove: Boolean = false

    override fun execute() {
        val targetBlock = player.getTargetBlock(TARGET_BLOCK_DISTANCE) ?: return
        val location = targetBlock.location
        location.strikeLightningEffect()
        location.y += 1
        registerDamage(location)
    }

    override val damage: BigInteger = defaultDamageScaling(player, DAMAGE)

    override val buildingsDamageRadius: Double = RADIUS
    override val entitiesDamageRadius: Double = RADIUS
    override val playersDamageRadius: Double = RADIUS

    companion object {
        const val RADIUS = 3.0
        const val TARGET_BLOCK_DISTANCE = 20
        val DAMAGE: BigDecimal = BigDecimal.valueOf(40.0)
    }
}

class Zeus_Earthquake(val player: Player) : SimulatorSkillExecution(player) {
    @DelicateCoroutinesApi
    override fun execute() {
        val targetBlock = player.getTargetBlock(DISTANCE) ?: return
        val location = targetBlock.location
        GlobalScope.launch {
            for (i in 0 until INTENSITY) {
                val randomLocation = Location(
                    location.world,
                    ((Math.random() - 0.5) * RADIUS) + location.x,
                    location.y + 1.5,
                    ((Math.random() - 0.5) * RADIUS) + location.z,
                )
                registerDamage(randomLocation)
                player.world.broadcast(PacketPlayOutExplosion(randomLocation.x, randomLocation.y, randomLocation.z, POWER, emptyList(), Vec3D.a))
                delay(EXPLOSION_DELAY)
            }
        }
    }

    override val buildingsDamageRadius: Double = DAMAGE_RADIUS
    override val entitiesDamageRadius: Double = DAMAGE_RADIUS
    override val playersDamageRadius: Double = DAMAGE_RADIUS

    override val repeatDamage: Boolean = true

    override val damage: BigInteger = defaultDamageScaling(player, DAMAGE)

    companion object {
        val DAMAGE: BigDecimal = BigDecimal.valueOf(5.0)
        const val DISTANCE = 15
        const val RADIUS = 5.0
        const val DAMAGE_RADIUS = 3.0
        const val INTENSITY: Int = 20
        const val POWER: Float = 1f
        const val EXPLOSION_DELAY: Long = 100
    }
}








class Apollo_SunBeam(val player: Player) : SimulatorSkillExecution(player) {
    override fun execute() {
        val from = player.eyeLocation
        val direction = from.direction
        particleBeam(from, direction, BEAM_PARTICLE, BEAM_RANGE, BEAM_SPEED, BEAM_SPACING, BEAM_PARTICLE_AMOUNT)
    }

    override fun onMove(to: Location) {
        damageBuildings = false
        registerDamage(to)
    }

    override fun onBlockHit(block: Block) {
        damageBuildings = true
        registerDamage(block.location)
        stop()
    }

    override var damageBuildings: Boolean = true

    override val damage: BigInteger = defaultDamageScaling(player, DAMAGE)

    companion object {
        const val BEAM_RANGE = 15.0
        const val BEAM_SPEED = 35.0
        const val BEAM_SPACING = 1.05
        val BEAM_PARTICLE: Particle = Particle.FLAME
        const val BEAM_PARTICLE_AMOUNT = 10
        val DAMAGE: BigDecimal = BigDecimal.valueOf(40.0)
    }
}

class Apollo_SolarBlast(val player: Player) : SimulatorSkillExecution(player) {
    @DelicateCoroutinesApi
    override fun execute() {
        val pair = player.getTargetLocation(DISTANCE, DEFAULT_TRANSPARENT) ?: return
        val location = pair.first

        GlobalScope.launch {
            location.world.spawnParticle(Particle.EXPLOSION_LARGE, location, PARTICLE_AMOUNT)
            location.world.spawnParticle(Particle.LAVA, location, PARTICLE_AMOUNT)
            delay(400)
            registerDamage(location)
        }
    }

    override val buildingsDamageRadius: Double = RADIUS
    override val entitiesDamageRadius: Double = RADIUS
    override val playersDamageRadius: Double = RADIUS

    override val damage: BigInteger = defaultDamageScaling(player, DAMAGE)

    companion object {
        val DAMAGE: BigDecimal = BigDecimal.valueOf(90.0)
        const val RADIUS = 5.0
        const val DISTANCE = 20
        const val PARTICLE_AMOUNT: Int = 100
    }
}

