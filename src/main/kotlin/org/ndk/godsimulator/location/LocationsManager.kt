package org.ndk.godsimulator.location


import com.sk89q.worldedit.regions.Region
import org.bukkit.Material
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.extension.readRegionOrThrow
import org.ndk.godsimulator.extension.readWEVector
import org.ndk.godsimulator.extension.toBukkitVector
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.world.SimulatorWorld
import org.ndk.klib.Constants
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.language.MSGHolder
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.plugin.ServerPlugin
import java.math.BigInteger

object LocationsManager : PluginModule {

    override val id: String = "LocationsManager"

    val locations = HashMap<String, SimulatorLocation>()
    val defaultLocations: HashSet<String> = HashSet()

    override fun onLoad(plugin: ServerPlugin) {
        val config = plugin.configsManager.load("locations")
        val worlds = GodSimulator.worlds
        config.forEachSectionSafe { loc ->
            val id = loc.name
            val name = loc.readMSGHolderOrThrow("name")
            val price = loc.readBigInteger("price", Constants.BIGINT_MINUS1)!!
            val material = loc.readMaterial("material", Material.STONE)!!
            val region = loc.readRegionOrThrow("region")

            // For each world, create a unique location object.
            // It cannot be shared between worlds because of WorldRegion requirements for a world.
            val locations = ArrayList<SimulatorLocation>(worlds.size).apply {
                for (world in worlds) {
                    add(
                        addLocation(id, name, world, region, price, material)
                    )
                }
            }

            loc.forEachSectionSafe("sell_zones") { zone ->
                val zoneId = zone.name
                val nameMSG = zone.readMSGHolder("name") ?: MSG.SELL_ZONE_MAIN_NAME
                val zoneRegion = zone.readRegionOrThrow("region")
                for (location in locations) {
                    val nameHologramVector = zone.readWEVector("hologramTranslation", location.region.center)!!.toBukkitVector()
                    location.addSellZone(zoneId, nameMSG, zoneRegion, nameHologramVector)
                }
            }
        }

        val default = config.getStringList("default")
        defaultLocations.addAll(default)
    }

    override fun onUnload(plugin: ServerPlugin) {
        locations.clear()
        defaultLocations.clear()
    }

    fun addLocation(id: String, name: MSGHolder, world: SimulatorWorld, region: Region, price: BigInteger, material: Material): SimulatorLocation {
        val location = SimulatorLocation(id, name, world, region, price, material)
        addLocation(location)
        return location
    }

    fun addLocation(location: SimulatorLocation) {
        locations[location.id] = location
        location.world.objectsManager.registerRegion(location)
    }

    fun getLocation(id: String): SimulatorLocation? {
        return locations[id]
    }
}