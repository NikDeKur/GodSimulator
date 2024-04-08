package org.ndk.godsimulator.world


import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.extension.readMSGOrThrow
import org.ndk.godsimulator.extension.readRegionOrThrow
import org.ndk.godsimulator.shop.ShopManager
import org.ndk.godsimulator.shop.ShopManager.Companion.readShopOrThrow
import org.ndk.minecraft.Utils.debug
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.modules.TaskMoment
import org.ndk.minecraft.movement.OptiPlayerMoveEvent
import org.ndk.minecraft.plugin.ServerPlugin
import org.spigotmc.event.player.PlayerSpawnLocationEvent

/**
 *
 * Depends on modules:
 * - ConfigsManager
 * - [via task] ShopManager
 * - [via task] Database
 */
class WorldsManager : PluginModule, Listener {
    val worlds = HashMap<String, SimulatorWorld>()
    val portals = ArrayList<Portal>()

    override val id: String = "WorldsManager"

    override fun onLoad(plugin: ServerPlugin) {
        GodSimulator.worldsManager = this

        val logger = plugin.logger
        val config = GodSimulator.configs.loadDefault()
        val gamerulesSection = config.getSection("gamerules")

        val worlds = Bukkit.getWorlds()
        gamerulesSection?.pairs?.forEach { (rule, value) ->
            try {
                val v = value.toString()
                worlds.forEach {
                    it.setGameRuleValue(rule, v)
                }
            } catch (e: Exception) {
                logger.warning("Error while setting gamerules")
                e.printStackTrace()
            }
        }



        for (worldBukkit in Bukkit.getWorlds()) {
            try {
                val world = SimulatorWorld(worldBukkit)
                addWorldData(world)

                val spawn = config.readLocationOrThrow("spawn", world = worldBukkit)
                world.spawnLocation = spawn

                // Load shops as scheduled task, because ShopManager loads after WorldsManager
                plugin.modulesManager.addTask(TaskMoment.AFTER_LOAD, "LoadShops_${worldBukkit.name}", ShopManager::class.java) {
                    config.readShopOrThrow("shop", world = worldBukkit)
                }

                config.getListSection("portals").forEach {
                    val name = it.readMSGOrThrow("name")
                    val hologramTranslation = it.readVector("hologramTranslation", VECTOR_ZERO)!!
                    val region = it.readRegionOrThrow("region")
                    val destination = it.readLocationOrThrow("destination", world = worldBukkit)
                    val portal = Portal(it.name, name, hologramTranslation, region, destination)
                    addPortal(world, portal)
                }

            } catch (e: Exception) {
                logger.warning("Error while loading world ${worldBukkit.name}!")
                e.printStackTrace()
            }
        }
    }

    override val unloadPriority: Int = -1
    override fun onUnload(plugin: ServerPlugin) {
        debug("Unloading WorldsManager")
        for (world in worlds.values) {
            world.objectsManager.onUnload()
        }
        worlds.clear()
        portals.clear()
    }

    fun getWorldData(world: String): SimulatorWorld? {
        return worlds[world]
    }

    fun addWorldData(world: SimulatorWorld) {
        worlds[world.bukkit.name] = world
    }


    fun addPortal(world: SimulatorWorld, portal: Portal) {
        portals.add(portal)
        portal.spawn()
        world.objectsManager.register(portal)
    }


    @EventHandler
    fun onPlayerSpawn(event: PlayerRespawnEvent) {
        val world = event.player.world
        val worldData = getWorldData(world.name) ?: return
        event.respawnLocation = worldData.spawnLocation
    }

    @EventHandler
    fun onPlayerSpawn(event: PlayerSpawnLocationEvent) {
        val world = event.player.world
        val worldData = getWorldData(world.name) ?: return
        event.spawnLocation = worldData.spawnLocation
    }

    @EventHandler
    fun onPlayerMove(event: OptiPlayerMoveEvent) {
        val to = event.to
        to.world
            .data
            .objectsManager
            .findSpecific(to, Portal::class.java)
            .firstOrNull()
            ?.teleport(event.player)
    }


    companion object {
        val World.data: SimulatorWorld
            get() = GodSimulator.worldsManager.getWorldData(name) ?: throw IllegalArgumentException("World $name is not loaded!")
    }

}