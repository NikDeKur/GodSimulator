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
import java.util.concurrent.CompletableFuture

abstract class SkillExecution(val executor: Entity) {

    open val listenMove: Boolean = true
    open val listenBlocks: Boolean = true

    var active: Boolean = true

    var distanceFlied: Double = 0.0

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
    fun particleBeam(from: Location, direction0: Vector, particle: Particle, range: Double, speed: Double, spacing: Double, spawnAmount: Number = 1): CompletableFuture<Unit> {
        val future = CompletableFuture<Unit>()
        val delayMillis = (500 / speed * spacing).toLong() // рассчитываем задержку

        val direction = direction0.clone()
        val location = from.clone()

        // debug("speed = $speed | space = $space | delay = $delayMillis")
        GlobalScope.launch {
            val world = location.world

            val repeat = (range / spacing).toInt()
            for (i in 1..repeat) {
                direction.multiply(spacing)
                location.add(direction)

                distanceFlied += spacing

                processEvents(location.clone())
                if (!active)
                    break

                world.spawnParticle(particle, location.x, location.y, location.z, spawnAmount.toInt(), 0.0, 0.0, 0.0, 0.0)

                delay(delayMillis)
            }

            future.complete(Unit)
        }

        return future
    }

}