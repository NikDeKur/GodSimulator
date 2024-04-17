package org.ndk.godsimulator.wobject

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.godsimulator.world.SimulatorWorld
import org.ndk.godsimulator.world.WorldsManager.data
import java.util.*

abstract class Object : HologramHolder, MSGNameHolder {

    open val id: UUID = UUID.randomUUID()
    abstract val location: Location

    val world: SimulatorWorld by lazy {
        location.world.data
    }

    /**
     * Hologram translation from the {@link #location}
     * Default is (0, 0, 0)
     *
     * For example, if you want to move the hologram up, you can use Vector(0.0, 1.0, 0.0)
     */
    open val hologramSpawnTranslation = Vector(0.0, 0.0, 0.0)
    abstract fun getHologramText(player: Player): Collection<String>

    @Suppress("LeakingThis")
    override val hologram = Hologram(this)


    /**
     * Spawn the object for everyone in the world
     *
     * This method is called on all objects loadings.
     *
     * The Default implementation is to show for everyone
     */
    open fun spawn() {
        showForEveryone()
    }

    /**
     * Show the object for the player
     *
     * This method is called when the player joins the world
     *
     * The Default implementation is to spawn the hologram for the player
     */
    open fun show(player: Player) {
        spawnHologram(player)
    }

    /**
     * Remove the object for everyone in the world
     *
     * This method is called on all objects unloading.
     *
     * The Default implementation is to remove the hologram from everyone
     */
    open fun remove() {
        removeHologram()
    }

    /**
     * Hide the object for the player
     *
     * This method is called when the player leaves the world
     *
     * The Default implementation is to remove the hologram from the player
     */
    open fun hide(player: Player) {
        removeHologram(player)
    }

    open fun update() {
        updateHologram()
    }
    open fun update(player: Player) {
        updateHologram(player)
    }


    override fun toString(): String {
        return "Object(id=$id, location=$location)"
    }


    open fun showForEveryone() {
        world.forEachPlayer(::show)
    }
    open fun hideForEveryone() {
        world.forEachPlayer(::hide)
    }





    companion object {

        class Hologram(val obj: Object) : org.ndk.minecraft.hologram.Hologram() {

            private var _loc: Location? = null
            val loc: Location
                get() = _loc ?: obj
                    .location
                    .clone()
                    .add(obj.hologramSpawnTranslation)
                    .also { _loc = it }

            override fun getLocation(player: Player) = loc

            override fun getText(player: Player) = obj.getHologramText(player)


        }
    }
}