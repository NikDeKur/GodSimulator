@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.extension

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.ndk.global.spatial.Point
import org.ndk.godsimulator.location.SimulatorLocation
import org.ndk.godsimulator.wobject.building.Building
import org.ndk.godsimulator.wobject.building.BuildingsManager
import org.ndk.godsimulator.wobject.entity.EntitiesManager
import org.ndk.godsimulator.world.WorldsManager.data
import org.ndk.minecraft.extension.asBlockLocation


inline val Location.simulatorLocation: SimulatorLocation?
    get() = world.data
        .objectsManager
        .findLocations(asBlockLocation())
        .firstOrNull()

inline fun Vector.toWEVector(): com.sk89q.worldedit.Vector {
    return com.sk89q.worldedit.Vector(this.x, this.y, this.z)
}
inline fun Location.toWEVector(): com.sk89q.worldedit.Vector {
    return com.sk89q.worldedit.Vector(this.x, this.y, this.z)
}

inline fun Location.getNearbyEntities(player: Player, radius: Double = 0.0): Collection<org.ndk.godsimulator.wobject.entity.Entity<*>> {
    return EntitiesManager.getEntities(player, this, radius)
}
inline fun Location.getNearbyBuildings(player: Player, radius: Double = 0.0): Set<Building> {
    return BuildingsManager.getBuildings(player, this, radius)
}

inline fun com.sk89q.worldedit.Vector.toBukkitVector(): Vector {
    return Vector(this.x, this.y, this.z)
}

inline fun com.sk89q.worldedit.Vector.toPoint(): Point {
    return Point(this.blockX, this.blockY, this.blockZ)
}

inline fun com.sk89q.worldedit.Vector.toLocation(world: org.bukkit.World): Location {
    return Location(world, this.x, this.y, this.z)
}

inline fun Point.toLocation(world: org.bukkit.World): Location {
    return Location(world, this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
}
