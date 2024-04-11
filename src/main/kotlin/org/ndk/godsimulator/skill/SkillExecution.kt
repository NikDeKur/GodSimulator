package org.ndk.godsimulator.skill

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.util.Vector
import org.ndk.klib.addIfNotContains

abstract class SkillExecution(val executor: Entity) {

    open val listenMove: Boolean = true
    open val listenBlocks: Boolean = true

    var active: Boolean = true

    var distanceFlied: Double = 0.0


    /**
     * Checks if the skill can be executed.
     *
     * Function is called before [highlightArea] and [execute].
     *
     * If highlightArea or execute may abort, it should be checked here.
     */
    abstract fun canExecute(): Boolean

    /**
     * Highlights the area where the skill will be executed.
     */
    abstract fun highlightArea()

    /**
     * Executes the skill.
     */
    abstract fun execute()


    private val blocksSet = HashSet<Location>()
    open fun onBlockHit(block: Block) {}

    open fun onMove(to: Location) {}


    private fun processEvents(location: Location) {
        if (listenMove)
            onMove(location)

        if (listenBlocks) {
            location.block.let {
                if (!it.type.isSolid) return@let
                if (blocksSet.addIfNotContains(it.location))
                    onBlockHit(it)
            }
        }
    }

    fun stop() {
        active = false
    }

    @OptIn(DelicateCoroutinesApi::class)
    /**
     * Creates a particle beam
     *
     * Particles spawn asynchrously using kotlin coroutines.
     * Particles spawn with no gravitation (extra = 0.0)
     *
     * @param from The location where the beam starts
     * @param direction The direction of the beam
     * @param particle The particle to spawn
     * @param range The range of the beam, how far the beam will go
     * @param speed The speed of the beam, how fast the beam will move (blocks per second)
     * @param spacing The spacing between particles in blocks
     * @param particleNumber The number of particles to spawn per-beam move in the world.spawnParticle
     */
    fun particleBeam(
        from: Location,
        direction: Vector,
        particle: Particle,
        range: Double,
        speed: Double,
        spacing: Double,
        particleNumber: Int = 1
    ) {
        val step = direction.clone().multiply(spacing)
        val steps = (range / spacing).toInt()

        GlobalScope.launch {
            for (i in 0 until steps) {
                processEvents(from)
                if (!active) break
                from.world.spawnParticle(particle, from.x, from.y, from.z, particleNumber, 0.0, 0.0, 0.0, 0.0, null)
                from.add(step)
                delay((1000 / speed).toLong())
            }
        }

    }

}