package org.ndk.godsimulator

import com.sk89q.worldedit.bukkit.WorldEditPlugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.ndk.godsimulator.command.LanguageCommand
import org.ndk.godsimulator.command.SaveBuildingCommand
import org.ndk.godsimulator.command.SpawnBuildingCommand
import org.ndk.godsimulator.command.TestCommand
import org.ndk.godsimulator.command.admin.AdminBaseCommand
import org.ndk.godsimulator.command.admin.AdminStickCommand
import org.ndk.godsimulator.database.Database
import org.ndk.godsimulator.economy.currency.CurrencyManager
import org.ndk.godsimulator.equipable.EquipableManager
import org.ndk.godsimulator.god.GodsManager
import org.ndk.godsimulator.language.LangManager
import org.ndk.godsimulator.language.SimulatorLangProvider
import org.ndk.godsimulator.listener.DoubleJumpManager
import org.ndk.godsimulator.listener.GlobalEventListener
import org.ndk.godsimulator.listener.OtherCommandsTabCompletion
import org.ndk.godsimulator.location.LocationComeListener
import org.ndk.godsimulator.location.LocationsManager
import org.ndk.godsimulator.menu.GlobalMenuListener
import org.ndk.godsimulator.quest.listening.GoalsListener
import org.ndk.godsimulator.quest.manager.QuestsManager
import org.ndk.godsimulator.reward.RewardsManager
import org.ndk.godsimulator.rpg.RPGListener
import org.ndk.godsimulator.rpg.RPGManager
import org.ndk.godsimulator.rpg.regen.RegenerationManager
import org.ndk.godsimulator.scoreboard.ScoreboardAdapter
import org.ndk.godsimulator.selling.SellZoneListener
import org.ndk.godsimulator.shop.ShopListener
import org.ndk.godsimulator.shop.ShopManager
import org.ndk.godsimulator.skill.cast.SkillListener
import org.ndk.godsimulator.wobject.building.BuildingsManager
import org.ndk.godsimulator.wobject.entity.EntitiesManager
import org.ndk.godsimulator.world.SimulatorWorld
import org.ndk.godsimulator.world.WorldsManager
import org.ndk.minecraft.Scheduler
import org.ndk.minecraft.Utils.debug
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
            OtherCommandsTabCompletion, SkillListener, GoalsListener,
            SellZoneListener, ShopListener,
            LocationComeListener, GlobalEventListener,
            AdminStickCommand.Companion.StickListener,
            RPGListener, GlobalMenuListener,

            // Commands
            TestCommand(), SpawnBuildingCommand(), LanguageCommand(),
            SaveBuildingCommand(), AdminBaseCommand(),

            // Modules
            BeforeModulesTask(),
            CurrencyManager, RewardsManager, LangManager,
            RPGManager, GodsManager,
            EquipableManager, Database, WorldsManager,
            ShopManager, BuildingsManager, EntitiesManager,
            LocationsManager, RegenerationManager, DoubleJumpManager,
            QuestsManager,
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

        inline val languagesManager: LanguagesManager
            get() = instance.languagesManager


        val worldedit: WorldEditPlugin = getPlugin(WorldEditPlugin::class.java)

        inline val worlds: Collection<SimulatorWorld>
            get() = WorldsManager.worlds.values
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