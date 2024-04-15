@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.database



import com.google.gson.Gson
import org.bukkit.GameMode
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitTask
import org.ndk.database.core.DatabaseV3
import org.ndk.database.service.AbstractDataService
import org.ndk.godsimulator.GodSimulator.Companion.database
import org.ndk.godsimulator.GodSimulator.Companion.logger
import org.ndk.godsimulator.GodSimulator.Companion.modulesManager
import org.ndk.godsimulator.GodSimulator.Companion.scheduler
import org.ndk.godsimulator.god.ForceGodSelectGUI
import org.ndk.godsimulator.world.WorldsManager
import org.ndk.godsimulator.world.WorldsManager.Companion.data
import org.ndk.klib.forEachSafe
import org.ndk.klib.toTArray
import org.ndk.minecraft.Utils.debug
import org.ndk.minecraft.extension.applyColors
import org.ndk.minecraft.extension.readDBConnector
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.modules.TaskMoment
import org.ndk.minecraft.plugin.ServerPlugin
import org.ndk.minecraft.plugin.ServerPlugin.Companion.bLogger
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates

object Database : PluginModule, Listener {

    const val SAVE_DELAY_TICKS = 6000L // 5 minutes
    const val ACCESSORS_CACHE_SIZE = 100
    const val LOADING_TIMEOUT = 3000L
    val getAttempt = AtomicInteger(0)

    override val id: String = "DatabaseManager"

    var core: DatabaseV3 by Delegates.notNull()

    lateinit var updateTask: BukkitTask

    var connectionEstablished = false



    override fun onLoad(plugin: ServerPlugin) {
        debug("Loading Database")
        database = this


        val connector = plugin.config.readDBConnector("database")
        core = DatabaseV3("GodSimulator-Main", connector)

        // Connect to the database
        core.checkConnection()
        connectionEstablished = true

        playersService.start()

        ServerPlugin.online.forEachSafe(::onStartError, ::startSession)

        updateTask = scheduler.runTaskTimerAsynchronously(SAVE_DELAY_TICKS, this::saveCachedPlayersDataAsync)
    }

    override fun onUnload(plugin: ServerPlugin) {
        updateTask.cancel()

        // Stop all sessions before service to make special actions on every player and session
        endAllSessionAndSaveDataSync()

        connectionEstablished = false

        playersService.stop()
        core.close()
    }


    fun onStartError(player: Player, e: Exception) {
        bLogger.warning("Error while starting session for player ${player.name}")
        e.printStackTrace()
    }

    fun onEndError(player: Player, e: Exception) {
        bLogger.warning("Error while ending session for player ${player.name}")
        e.printStackTrace()
    }

    val sessions = HashSet<UUID>()
    fun startSession(player: Player) {
        debug("Starting session for ${player.name}")
        if (sessions.contains(player.uniqueId))
            return
        sessions.add(player.uniqueId)

        // Will start loading the player data synchronously
        val accessor = player.accessor

        modulesManager.addTask(TaskMoment.AFTER_LOAD, "loadPlayerWorlds_${player.uniqueId}", WorldsManager::class.java) {
            val world = player.world.data

            // Update World Holograms
            scheduler.runTask {
                world.objectsManager.showForPlayer(player)
            }
        }

        val profile = accessor.profile

        scheduler.runTask {
            // Check For ForceGodSelect
            if (profile.forceSelectGod)
                ForceGodSelectGUI(player, profile).open()

            // Select 7 HotBat slot
            if (!profile.passSkillCast)
                player.inventory.heldItemSlot = 7

            // Set Adventure Mode
            if (!profile.passAdventure)
                player.gameMode = GameMode.ADVENTURE
        }
    }

    fun endSession(player: Player): PlayerAccessor? {
        debug("Ending session for ${player.name}")
        if (!sessions.contains(player.uniqueId)) return null

        val world = player.world.data

        sessions.remove(player.uniqueId)

        world.objectsManager.hideForPlayer(player)

        val accessor = player.accessor
        accessor.selectedProfile.onUnSelected()

        return saveAndUnloadAccessor(player)
    }

    fun endAllSessionAndSaveDataSync() {
        CompletableFuture.allOf(
            *ServerPlugin.online
                .mapNotNull {
                    try {
                        endSession(it)?.saving
                    } catch (e: Exception) {
                        onEndError(it, e)
                        null
                    }
                }
                .toTArray()
        ).join()
    }





    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage = null
        startSession(event.player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        endSession(event.player)
    }






    @Synchronized
    fun savePlayersDataAsync(accessors: Iterable<PlayerAccessor>): CompletableFuture<Void> {
        val futures = accessors.map { it.saveData() }.toTypedArray()

        return CompletableFuture.allOf(*futures)
    }

    fun saveCachedPlayersDataAsync(): CompletableFuture<Void> = savePlayersDataAsync(playersService.sessions.values)


    fun loadCachedPlayersDataAsync(): CompletableFuture<Void> {
        return CompletableFuture.allOf(*playersService.sessions.values.map { it.loadData() }.toTypedArray())
    }


    fun saveAndUnloadAccessor(player: OfflinePlayer): PlayerAccessor? {
        return playersService.stopSession(player.uniqueId)
    }


    val playersService = object : AbstractDataService<OfflinePlayer, UUID, PlayerAccessor>() {
        override val database by ::core
        override val tableName: String = "players"

        override val gson: Gson by lazy { GSON.gson }

        override fun createSession(holder: OfflinePlayer): PlayerAccessor {
            return PlayerAccessor(this, holder)
        }

        override val idSQLType: String = "VARCHAR(36)"
        override val sessionsLimit: Int = ACCESSORS_CACHE_SIZE

        override fun getName(holder: OfflinePlayer): String {
            return holder.name
        }

        override fun getId(holder: OfflinePlayer): UUID {
            return holder.uniqueId
        }
    }

//    val worldsService = object: AbstractDataService<World, UUID, SimulatorWorld>() {
//        override val database: DatabaseV3 = core
//        override val tableName: String = "worlds"
//
//        override val gson: Gson = GSON
//
//        override fun createSession(holder: World): SimulatorWorld {
//            return SimulatorWorld(this, holder)
//        }
//
//        override val idSQLType: String = "VARCHAR(36)"
//        override val sessionsLimit: Int = 100
//
//        override fun getName(holder: World): String {
//            return holder.name
//        }
//
//        override fun getId(holder: World): UUID {
//            return holder.uid
//        }
//    }




    val OfflinePlayer.accessorRaw: PlayerAccessor
        get() = database.playersService.getSession(this)

    @get:Synchronized
    val OfflinePlayer.accessorAsync: CompletableFuture<PlayerAccessor>
        get() {
            return if (database.connectionEstablished) {
                database.playersService.getSessionAsync(this)
            } else {
                // If connection is not established, it means the database hasn't loaded, yet
                // We add the task, which will be executed after the database is loaded
                // Task will ask for async accessor and return it
                val future = CompletableFuture<PlayerAccessor>()
                val taskId = "getAccessorAsync_${getAttempt.incrementAndGet()}"
                modulesManager.addTask(TaskMoment.AFTER_LOAD, taskId, Database::class.java) { db ->
                    db.playersService.getSessionAsync(this)
                        .thenAccept { future.complete(it) }
                }
                future
            }
        }

    @get:Synchronized
    val Player.accessor: PlayerAccessor
        get() {
            require(database.connectionEstablished) { "Database is not yet ready to loadData" }

            val accessor = accessorRaw

            return if (accessor.isLoaded)
                accessor
            else {
                val async = accessorAsync

                try {
                    async.get(LOADING_TIMEOUT, TimeUnit.MILLISECONDS)
                } catch (e: TimeoutException) {
                    logger.warning("Can't load player data for $uniqueId ($name) in $LOADING_TIMEOUT milliseconds.")
                    e.printStackTrace()
                    kickPlayer("&cCan't load your data. Try to reconnect.".applyColors())
                    throw e
                }
                accessor
            }
        }


    /**
     * Returns loaded player accessor
     *
     * If the database is not connected, returns null
     *
     * If the accessor is not loaded (calling this don't start loading), returns null
     */
    @get:Synchronized
    val OfflinePlayer.loadedAccessor: PlayerAccessor?
        get() {
            if (!database.connectionEstablished) return null
            val accessor = accessorRaw
            return if (accessor.isLoaded) accessor else null
        }



}

typealias PlayersDataService = AbstractDataService<OfflinePlayer, UUID, PlayerAccessor>
// typealias WorldsDataService = AbstractDataService<World, UUID, SimulatorWorld>