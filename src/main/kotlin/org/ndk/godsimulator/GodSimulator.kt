package org.ndk.godsimulator

import com.sk89q.worldedit.bukkit.WorldEditPlugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.ndk.godsimulator.command.*
import org.ndk.godsimulator.command.admin.AdminBaseCommand
import org.ndk.godsimulator.command.admin.AdminStickCommand
import org.ndk.godsimulator.database.Database
import org.ndk.godsimulator.equipable.EquipableManager
import org.ndk.godsimulator.god.GodsManager
import org.ndk.godsimulator.language.SimulatorLangManager
import org.ndk.godsimulator.language.SimulatorLangProvider
import org.ndk.godsimulator.listener.DoubleJumpManager
import org.ndk.godsimulator.listener.GlobalEventListener
import org.ndk.godsimulator.listener.OtherCommandsTabCompletion
import org.ndk.godsimulator.location.LocationComeListener
import org.ndk.godsimulator.location.LocationsManager
import org.ndk.godsimulator.menu.GlobalMenuListener
import org.ndk.godsimulator.rpg.RPGListener
import org.ndk.godsimulator.rpg.RPGManager
import org.ndk.godsimulator.rpg.regen.RegenerationManager
import org.ndk.godsimulator.scoreboard.ScoreboardAdapter
import org.ndk.godsimulator.selling.SellZoneListener
import org.ndk.godsimulator.shop.ShopListener
import org.ndk.godsimulator.shop.ShopManager
import org.ndk.godsimulator.skill.cast.SkillCastListener
import org.ndk.godsimulator.wobject.building.BuildingsManager
import org.ndk.godsimulator.wobject.entity.EntitiesManager
import org.ndk.godsimulator.world.SimulatorWorld
import org.ndk.godsimulator.world.WorldsManager
import org.ndk.minecraft.Scheduler
import org.ndk.minecraft.Utils.debug
import org.ndk.minecraft.config.ConfigsManager
import org.ndk.minecraft.language.LanguageProvider
import org.ndk.minecraft.language.LanguagesManager
import org.ndk.minecraft.modules.ModulesManager
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.plugin.ServerPlugin
import java.util.logging.Logger
import kotlin.properties.Delegates


class GodSimulator : ServerPlugin() {

    override fun afterReload() {
        debug("GodSimulator enabled")
    }

    override fun beforeReload() {
        debug("GodSimulator disabled")
    }

    override fun whenLoad() {
        instance = this
        Companion.logger = logger
        Companion.scheduler = scheduler
    }


    override val preLoadingClassesBL: Set<String>
        get() = setOf(
            "org.ndk.godsimulator.extension.Patterns"
        )
    override val preLoadingClassesWL: Set<String>
        get() = setOf("org.mariadb.jdbc.internal.com.send.SendClosePacket", "kotlin.collections.ArrayAsCollection")

    override val components
        get() = listOf(
            // Listeners
            OtherCommandsTabCompletion(), SkillCastListener(),
            SellZoneListener(), ShopListener(),
            LocationComeListener(), GlobalEventListener(),
            AdminStickCommand.Companion.StickListener(),
            RPGListener(), GlobalMenuListener(),

            // Commands
            TestCommand(), SpawnBuildingCommand(), LanguageCommand(),
            SaveBuildingCommand(), AdminBaseCommand(), ProfilesCommand(),
            PetsCommand(),

            // Modules
            BeforeModulesTask(),
            SimulatorLangManager(), RPGManager(), GodsManager(),
            EquipableManager(), Database(), WorldsManager(),
            ShopManager(), BuildingsManager(), EntitiesManager(),
            LocationsManager(), RegenerationManager, DoubleJumpManager,
            org.ndk.minecraft.scoreboard.ScoreboardManager(ScoreboardAdapter())
        )
    override val languageProvider: LanguageProvider = SimulatorLangProvider()

    companion object {

        val classLoader: ClassLoader
            get() = instance.classLoader

        lateinit var instance: GodSimulator

        lateinit var logger: Logger
        lateinit var scheduler: Scheduler
        lateinit var modulesManager: ModulesManager

        lateinit var godsManager: GodsManager

        val configs: ConfigsManager
            get() = instance.modulesManager.getModule("ConfigsManager") as ConfigsManager

        inline val languagesManager: LanguagesManager
            get() = instance.languagesManager

        lateinit var rpgManager: RPGManager
        lateinit var equipableManager: EquipableManager
        lateinit var shopManager: ShopManager
        lateinit var worldsManager: WorldsManager
        lateinit var buildingsManager: BuildingsManager
        lateinit var entitiesManager: EntitiesManager
        lateinit var database: Database
        lateinit var locationsManager: LocationsManager


        val worldedit: WorldEditPlugin
            get() = getPlugin(WorldEditPlugin::class.java)

        val worlds: Collection<SimulatorWorld>
            get() = worldsManager.worlds.values
    }

    class BeforeModulesTask : PluginModule {
        override val id: String = "BeforeModulesTask"

        override fun onLoad(plugin: ServerPlugin) {
            Companion.modulesManager = plugin.modulesManager

            // Remove all entities except players
            for (world in Bukkit.getWorlds()) {
                for (entity in world.entities) {
                    if (entity !is Player) {
                        entity.remove()
                    }
                }
            }

            val config = plugin.configsManager.loadDefault()

            // Set properties
            Properties.playerCombatDelay = config.getLong("player_combat_delay", 5000)
        }
    }


    class Properties {
        companion object {
            var playerCombatDelay by Delegates.notNull<Long>()
        }
    }
}