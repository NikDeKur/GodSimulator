@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.wobject.building


import com.sk89q.worldedit.Vector
import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.regions.Region
import com.sk89q.worldedit.world.World
import org.bukkit.Location
import org.bukkit.entity.Player
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.GodSimulator.Companion.scheduler
import org.ndk.godsimulator.world.WorldsManager.data
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.plugin.ServerPlugin
import java.io.File
import java.math.BigInteger
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Logger


object BuildingsManager : PluginModule {


    val patterns = ConcurrentHashMap<String, BuildingPattern>()
    val buildings = ConcurrentHashMap<UUID, Building>()

    lateinit var logger: Logger

    lateinit var patternsDirectory: File
    override val id: String = "BuildingsManager"


    override fun onLoad(plugin: ServerPlugin) {
        patternsDirectory = plugin.dataFolder.resolve("patterns")
        if (!patternsDirectory.exists()) {
            patternsDirectory.mkdirs()
        }
        logger = plugin.logger

        registerBuildings(plugin)
    }

    override fun onUnload(plugin: ServerPlugin) {
        buildings.clear()
        patterns.clear()
        GodSimulator.worlds.forEach {
            it.objectsManager.removeBuildings()
        }
    }


    fun addBuilding(building: Building) {
        buildings[building.id] = building
        building.world.objectsManager.register(building)
    }

    fun addBuilding(
        pattern: BuildingPattern,
        respawnTicks: Long,
        location: Location,
        level: Int,
        maxHealth: BigInteger,
        expDrop: BigInteger,
        bagFill: BigInteger,
    ) {
        // Building spawns in init of class
        val building = Building(pattern, respawnTicks, location, level, maxHealth, expDrop, bagFill)
        addBuilding(building)
    }

    fun removeBuilding(id: UUID) {
        val building = buildings.remove(id) ?: return
        building.world.objectsManager.unregister(building)
    }

    fun removeBuilding(building: Building) {
        buildings.remove(building.id)
    }


    fun saveBuilding(origin: Vector, location1: Location, location2: Location, name: String) {
        val world = BukkitWorld(location1.world) as World
        val region = CuboidRegion(world,
            Vector(location1.x, location1.y, location1.z),
            Vector(location2.x, location2.y, location2.z)
        )
        saveBuilding(region, origin, name)
    }

    fun saveBuilding(region: Region, origin: Vector, name: String) {
        saveBuilding(region, origin, patternsDirectory.resolve("$name.schematic"))
    }

    fun saveBuilding(region: Region, origin: Vector, file: File) {
        scheduler.runTaskAsynchronously {
            WorldEditAPI.saveSchematic(region, origin, file)
        }
    }

    fun pasteBuilding(location: Location, schematic: Clipboard, side: WorldEditAPI.WorldSide = WorldEditAPI.WorldSide.EAST): Region {
        return WorldEditAPI.pasteSchematic(location, schematic, side)
    }

    fun pasteBuilding(location: Location, name: String, side: WorldEditAPI.WorldSide = WorldEditAPI.WorldSide.EAST): Region {
        return WorldEditAPI.pasteSchematic(location, patternsDirectory.resolve("$name.schematic"), side)
    }

    fun loadSchematic(schemName: String): Clipboard {
        return WorldEditAPI.loadSchematic(patternsDirectory.resolve("$schemName.schematic"))
    }





    fun registerBuildings(plugin: ServerPlugin) {
        val patternsCFG = plugin.configsManager.load("buildings_patterns")
        for (it in patternsCFG.getListSection()) {
            val id = it.name
            val name = it.readMSGHolderOrThrow("name")
            val schemName = it.getStringOrThrow("schematic")
            val hpHologramVector = it.readVector("hologramTranslation", VECTOR_ZERO)!!
            patterns[id] = BuildingPattern(this, id, name, schemName, hpHologramVector)
        }


        val worlds = GodSimulator.worlds
        val buildingsCFG = plugin.configsManager.load("buildings")
        for (buildingS in buildingsCFG.getListSection()) {
            val patternId = buildingS.getStringOrThrow("pattern")
            val pattern = patterns[patternId]
            if (pattern == null) {
                plugin.logger.warning("Error while loading building ${buildingS.name}! Pattern ($patternId) not found!")
                continue
            }
            val respawnTicks = buildingS.getLongOrThrow("respawnTicks")
            val level = buildingS.getInt("level", 1)
            val location = buildingS.readAbstractLocationOrThrow("location")
            val maxHealth = buildingS.readBigIntegerOrThrow("health")
            val expDrop = buildingS.readBigIntegerOrThrow("xp")
            val bagFill = buildingS.readBigIntegerOrThrow("bagFill")

            for (world in worlds) {
                addBuilding(pattern, respawnTicks, location.toLocation(world.bukkit), level, maxHealth, expDrop, bagFill)
            }
        }
    }


    fun getBuildings(player: Player, x: Int, y: Int, z: Int, radius: Double = 0.0): HashSet<Building> {
        val world = player.world.data
        val buildings = if (radius <= 0.0) {
            world.objectsManager.findBuildings(x, y, z)
        } else {
            world.objectsManager.findNearBuildings(x, y, z, radius)
        }

        buildings.removeIf { it.isKilled }
        return buildings
    }

    inline fun getBuildings(player: Player, location: Location, radius: Double = 0.0): HashSet<Building> {
        return getBuildings(player, location.blockX, location.blockY, location.blockZ, radius)
    }
}

