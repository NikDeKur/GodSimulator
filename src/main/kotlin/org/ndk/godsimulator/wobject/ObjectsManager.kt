@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.wobject

import org.bukkit.Location
import org.bukkit.entity.Player
import org.ndk.godsimulator.location.SimulatorLocation
import org.ndk.godsimulator.selling.SellZone
import org.ndk.godsimulator.shop.Shop
import org.ndk.godsimulator.wobject.building.Building
import org.ndk.godsimulator.wobject.entity.Entity
import org.ndk.godsimulator.world.SimulatorWorld
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ObjectsManager(val world: SimulatorWorld) {

    /**
     * All living objects in the world
     * A HashMap with UUID as a key and LivingObject as value
     */
    val objects = ConcurrentHashMap<UUID, Object>()
    fun register(obj: Object) {
        objects[obj.id] = obj
        registerRegion(obj)

    }
    fun unregister(obj: Object) {
        objects.remove(obj.id)
    }
    fun get(id: UUID): Object? {
        return objects[id]
    }

    fun updateForEveryone() {
        for (obj in objects.values) {
            obj.update()
        }
    }

    fun updateForPlayer(player: Player) {
        for (obj in objects.values) {
            obj.update(player)
        }
    }


    fun spawnForEveryone() {
        for (obj in objects.values) {
            obj.spawn()
        }
    }

    fun showForPlayer(player: Player) {
        for (obj in objects.values) {
            obj.show(player)
        }
    }



    fun removeForEveryone() {
        for (obj in objects.values) {
            obj.remove()
        }
    }

    fun hideForPlayer(player: Player) {
        for (obj in objects.values) {
            obj.hide(player)
        }
    }


    val objectsTree = ObjectsTree(this, 10)
    fun registerRegion(obj: Region) {
        objectsTree.insert(obj)
    }

    inline fun <T : Region> findSpecific(to: Location, clazz: Class<T>): HashSet<T> {
        return objectsTree.findSpecific(to, clazz)
    }

    fun findObjects(x: Int, y: Int, z: Int) = objectsTree.findSpecific(x, y, z, Object::class.java)
    inline fun findObjects(location: Location) = findObjects(location.blockX, location.blockY, location.blockZ)

    fun findLivingObjects(x: Int, y: Int, z: Int) = objectsTree.findSpecific(x, y, z, LivingObject::class.java)
    inline fun findLivingObjects(location: Location) = findLivingObjects(location.blockX, location.blockY, location.blockZ)

    fun findEntities(x: Int, y: Int, z: Int) = objectsTree.findSpecific(x, y, z, Entity::class.java)
    inline fun findEntities(location: Location) = findEntities(location.blockX, location.blockY, location.blockZ)

    fun findBuildings(x: Int, y: Int, z: Int) = objectsTree.findSpecific(x, y, z, Building::class.java)
    inline fun findBuildings(location: Location) = findBuildings(location.blockX, location.blockY, location.blockZ)

    fun findSellZones(x: Int, y: Int, z: Int) = objectsTree.findSpecific(x, y, z, SellZone::class.java)
    inline fun findSellZones(location: Location) = findSellZones(location.blockX, location.blockY, location.blockZ)

    fun findLocations(x: Int, y: Int, z: Int) = objectsTree.findSpecific(x, y, z, SimulatorLocation::class.java)
    inline fun findLocations(location: Location) = findLocations(location.blockX, location.blockY, location.blockZ)



    fun findNearObjects(x: Int, y: Int, z: Int, radius: Double) = objectsTree.findNearSpecific(x, y, z, radius, Object::class.java)
    inline fun findNearObjects(location: Location, radius: Double) = findNearObjects(location.blockX, location.blockY, location.blockZ, radius)

    fun findNearLivingObjects(x: Int, y: Int, z: Int, radius: Double) = objectsTree.findNearSpecific(x, y, z, radius, LivingObject::class.java)
    inline fun findNearLivingObjects(location: Location, radius: Double) = findNearLivingObjects(location.blockX, location.blockY, location.blockZ, radius)

    fun findNearEntities(x: Int, y: Int, z: Int, radius: Double): HashSet<Entity<*>> = objectsTree.findNearSpecific(x, y, z, radius, Entity::class.java)
    inline fun findNearEntities(location: Location, radius: Double) = findNearEntities(location.blockX, location.blockY, location.blockZ, radius)

    fun findNearBuildings(x: Int, y: Int, z: Int, radius: Double): HashSet<Building> = objectsTree.findNearSpecific(x, y, z, radius, Building::class.java)
    inline fun findNearBuildings(location: Location, radius: Double) = findNearBuildings(location.blockX, location.blockY, location.blockZ, radius)

    fun findNearSellZones(x: Int, y: Int, z: Int, radius: Double) = objectsTree.findNearSpecific(x, y, z, radius, SellZone::class.java)
    inline fun findNearSellZones(location: Location, radius: Double) = findNearSellZones(location.blockX, location.blockY, location.blockZ, radius)

    fun findNearLocations(x: Int, y: Int, z: Int, radius: Double) = objectsTree.findNearSpecific(x, y, z, radius, SimulatorLocation::class.java)
    inline fun findNearLocations(location: Location, radius: Double) = findNearLocations(location.blockX, location.blockY, location.blockZ, radius)



    fun findObjectsInRegion(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) = objectsTree.findSpecificInRegion(x1, y1, z1, x2, y2, z2, Object::class.java)
    inline fun findObjectsInRegion(loc1: Location, loc2: Location) = findObjectsInRegion(loc1.blockX, loc1.blockY, loc1.blockZ, loc2.blockX, loc2.blockY, loc2.blockZ)

    fun findLivingObjectsInRegion(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) = objectsTree.findSpecificInRegion(x1, y1, z1, x2, y2, z2, LivingObject::class.java)
    inline fun findLivingObjectsInRegion(loc1: Location, loc2: Location) = findLivingObjectsInRegion(loc1.blockX, loc1.blockY, loc1.blockZ, loc2.blockX, loc2.blockY, loc2.blockZ)

    fun findEntitiesInRegion(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) = objectsTree.findSpecificInRegion(x1, y1, z1, x2, y2, z2, Entity::class.java)
    inline fun findEntitiesInRegion(loc1: Location, loc2: Location) = findEntitiesInRegion(loc1.blockX, loc1.blockY, loc1.blockZ, loc2.blockX, loc2.blockY, loc2.blockZ)

    fun findBuildingsInRegion(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) = objectsTree.findSpecificInRegion(x1, y1, z1, x2, y2, z2, Building::class.java)
    inline fun findBuildingsInRegion(loc1: Location, loc2: Location) = findBuildingsInRegion(loc1.blockX, loc1.blockY, loc1.blockZ, loc2.blockX, loc2.blockY, loc2.blockZ)

    fun findSellZonesInRegion(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) = objectsTree.findSpecificInRegion(x1, y1, z1, x2, y2, z2, SellZone::class.java)
    inline fun findSellZonesInRegion(loc1: Location, loc2: Location) = findSellZonesInRegion(loc1.blockX, loc1.blockY, loc1.blockZ, loc2.blockX, loc2.blockY, loc2.blockZ)

    fun findLocationsInRegion(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) = objectsTree.findSpecificInRegion(x1, y1, z1, x2, y2, z2, SimulatorLocation::class.java)
    inline fun findLocationsInRegion(loc1: Location, loc2: Location) = findLocationsInRegion(loc1.blockX, loc1.blockY, loc1.blockZ, loc2.blockX, loc2.blockY, loc2.blockZ)

    fun removeLivingObjects() = objectsTree.removeSpecific(LivingObject::class.java)
    fun removeEntities() = objectsTree.removeSpecific(Entity::class.java)
    fun removeBuildings() = objectsTree.removeSpecific(Building::class.java)
    fun removeSellZones() = objectsTree.removeSpecific(SellZone::class.java)
    fun removeLocations() = objectsTree.removeSpecific(SimulatorLocation::class.java)
    fun removeShops() = objectsTree.removeSpecific(Shop::class.java)


    fun onUnload() {
        for (item in objects.values) {
            item.remove()
        }

        objectsTree.clear()
        objects.clear()
    }



}