@file:Suppress("NOTHING_TO_INLINE", "OVERRIDE_BY_INLINE")

package org.ndk.godsimulator.wobject

import org.bukkit.Location
import org.ndk.global.spatial.Octree
import org.ndk.klib.removeIfNotAssignable

class ObjectsTree(val manager: ObjectsManager, capacity: Int) : Octree<Region>(capacity) {


    inline fun find(location: Location): HashSet<Region> {
        return find(location.blockX, location.blockY, location.blockZ)
    }
    inline fun findNearby(location: Location, radius: Double): HashSet<Region> {
        return findNearby(location.blockX, location.blockY, location.blockZ, radius)
    }

    override inline fun contains(obj: Region, x: Int, y: Int, z: Int) = obj.contains(x, y, z)
    override inline fun getMaxPoint(o: Region) = o.maxPoint
    override inline fun getMinPoint(o: Region) = o.minPoint

    @Suppress("UNCHECKED_CAST")
    inline fun <T : Region> findSpecific(x: Int, y: Int, z: Int, clazz: Class<T>): HashSet<T> {
        val find = find(x, y, z)
        find.removeIfNotAssignable(clazz)
        return find as HashSet<T>
    }
    inline fun <T : Region> findSpecific(location: Location, clazz: Class<T>): HashSet<T> {
        return findSpecific(location.blockX, location.blockY, location.blockZ, clazz)
    }



    @Suppress("UNCHECKED_CAST")
    inline fun <T : Region> findNearSpecific(x: Int, y: Int, z: Int, radius: Double, clazz: Class<T>): HashSet<T> {
        val find = findNearby(x, y, z, radius)
        find.removeIfNotAssignable(clazz)
        return find as HashSet<T>
    }

    inline fun <T : Region> findNearSpecific(location: Location, radius: Double, clazz: Class<T>): HashSet<T> {
        return findNearSpecific(location.blockX, location.blockY, location.blockZ, radius, clazz)
    }


    @Suppress("UNCHECKED_CAST")
    inline fun <T : Region> findSpecificInRegion(minX: Int, minY: Int, minZ: Int, maxX: Int, maxY: Int, maxZ: Int, clazz: Class<T>): HashSet<T> {
        val find = findInRegion(minX, minY, minZ, maxX, maxY, maxZ)
        find.removeIfNotAssignable(clazz)
        return find as HashSet<T>
    }

    inline fun <T : Region> findSpecificInRegion(min: Location, max: Location, clazz: Class<T>): HashSet<T> {
        return findSpecificInRegion(min.blockX, min.blockY, min.blockZ, max.blockX, max.blockY, max.blockZ, clazz)
    }



    fun <T : Region> removeSpecific(clazz: Class<T>) {
        removeIf {
            val res = clazz.isInstance(it)
            if (res) {
                if (it is Object) {
                    it.remove()
                    manager.objects.remove(it.id)
                }
            }
            res
        }
    }

	// Note: 
	// 1. Call to super is required to find entities that cannot be found by Int locations.
	// 2. The Method described above can cause problems because of adding to result structures without checking the block contact
    override fun findNearby(x: Int, y: Int, z: Int, radius: Double): java.util.HashSet<Region> {
        val result = super.findNearby(x, y, z, radius)
        val world = manager.world.bukkit

        val radiusInt = radius.toInt()
        for (iX in -radiusInt..radiusInt) {
            val fX = x + iX
            for (iY in -radiusInt..radiusInt) {
                val fY = y + iY
                for (iZ in -radiusInt..radiusInt) {
                    val fZ = z + iZ
                    if (!world.getBlockAt(fX, fY, fZ).type.isSolid) continue
                    result.addAll(find(fX, fY, fZ))
                }
            }
        }

        return result
    }
}

