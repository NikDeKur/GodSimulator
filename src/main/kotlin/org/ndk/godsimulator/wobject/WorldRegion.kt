package org.ndk.godsimulator.wobject

import org.ndk.global.spatial.Point
import org.ndk.godsimulator.selling.SellZone
import org.ndk.godsimulator.wobject.building.Building
import org.ndk.godsimulator.world.Portal
import org.ndk.godsimulator.world.SimulatorWorld


/**
 * An interface for objects that have a **static** region in the world.
 *
 * Objects implementing this interface can be registered and found via [ObjectsManager].
 *
 * Usually the interface used by simulator objects, such as [Building], [SellZone], [Portal], etc.
 * Previous was part of [Object], but was moved to a separate interface to allow moveable objects to not have a region.
 *
 * @see ObjectsManager
 */
interface WorldRegion {

    val world: SimulatorWorld

    val minPoint: Point
    val maxPoint: Point

    fun contains(x: Int, y: Int, z: Int): Boolean
}