package org.ndk.godsimulator.wobject

import org.ndk.global.spatial.Point
import org.ndk.godsimulator.world.SimulatorWorld

interface Region {

    val world: SimulatorWorld

    val minPoint: Point
    val maxPoint: Point

    fun contains(x: Int, y: Int, z: Int): Boolean
}