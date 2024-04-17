package org.ndk.godsimulator.command

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.Material
import org.bukkit.block.Chest
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.ndk.database.core.queue.Query
import org.ndk.global.math.RealRange
import org.ndk.global.tools.Tools
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.GodSimulator.Companion.languagesManager
import org.ndk.godsimulator.GodSimulator.Companion.scheduler
import org.ndk.godsimulator.database.Database
import org.ndk.godsimulator.database.Database.accessor
import org.ndk.godsimulator.database.Database.accessorAsync
import org.ndk.godsimulator.database.GSON
import org.ndk.godsimulator.equipable.EquipableManager
import org.ndk.godsimulator.extension.sendSimulatorMessage
import org.ndk.godsimulator.extension.simulatorLocation
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profile
import org.ndk.godsimulator.quest.goal.abc.DealDamageGoalPattern
import org.ndk.godsimulator.rpg.stat.RPGHealthStat
import org.ndk.godsimulator.shop.Shop
import org.ndk.godsimulator.wobject.building.BuildingsManager
import org.ndk.godsimulator.world.WorldsManager.data
import org.ndk.klib.*
import org.ndk.minecraft.Utils.debug
import org.ndk.minecraft.Utils.debugAverageExecTime
import org.ndk.minecraft.Utils.debugExecTime
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.CommandTabExecution
import org.ndk.minecraft.config.ConfigsManager
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.gui.GUIManager
import org.ndk.minecraft.language.Language
import org.ndk.minecraft.language.MSGHolder
import org.ndk.minecraft.npc.NPCManager
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.CompletableFuture

@Suppress("unused", "UNUSED_PARAMETER")
class TestCommand : SimulatorCommand() {

    val optionsStr: List<String>
    val options: Map<String, Method> = r_ClassMethods
        .filter {
            val method = it.value
            method.parameterCount == 1 && method.parameterTypes[0] == CommandExecution::class.java
        }
        .also { op -> optionsStr = op.values.map { it.name } }


    override fun onCommand(execution: CommandExecution) {
        val option = execution.getArg(0)
        val method = options[option]
        if (method == null) {
            execution.sendSimulatorMessage("Unknown test option '$option'! Use tab complete to see available options.")
            return
        }
        method.invoke(this, execution)
    }

    override fun onTabComplete(execution: CommandTabExecution): MutableList<String>? {
        return if (execution.argsSize == 1) optionsStr.toMutableList() else null
    }

    fun giveDamageQuest(execution: CommandExecution) {
        val player = if (execution.isConsole) execution.getOfflinePlayer(1) else execution.player
        player.accessorAsync.thenAccept {
            val quest = it.profile.quests.newQuest(MSG.QUEST_TEST_NAME, MSG.QUEST_TEST_DESCRIPTION)
            quest.addGoal(DealDamageGoalPattern(Constants.BIGINT_100))
            debug("Quest created")
        }
    }


    fun buffsAttaches(execution: CommandExecution) {
        val player = execution.player
        val profile = player.profile
        val rpg = profile.rpg
        val buffs = rpg.buffs
        debug(buffs.attaches)
    }

    fun isInCombat(execution: CommandExecution) {
        val player = execution.player
        val profile = player.profile
        val rpg = profile.rpg
        val combatTracker = rpg.combatTracker
        debug(combatTracker.isInCombat)
    }

    fun playerLanguage(execution: CommandExecution) {
        debug(execution.player.locale)
    }

    fun runtimeProfiles(execution: CommandExecution) {
        debug(execution.player.accessor.profiles)
    }

    fun giveSkull(execution: CommandExecution) {
        val itemStack = ItemStack(Material.SKULL_ITEM, 1, 3)
        val meta = itemStack.itemMeta as SkullMeta
        val profile = GameProfile(UUID.randomUUID(), null)
        profile.properties.put("textures", Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19"))
        meta.r_SetField("profile", profile)
        itemStack.itemMeta = meta
        execution.player.inventory.addItem(itemStack)
    }

    fun removeHealthBuffs(execution: CommandExecution) {
        val player = execution.player
        val profile = player.profile
        val rpg = profile.rpg
        rpg.buffs.removeBuffs(RPGHealthStat)
    }


    fun damageSelfRaw(execution: CommandExecution) {
        val player = execution.player
        val damage = execution.getBigDecimal(1)
        player.profile.rpg.damageRaw(damage, false)
    }


    fun giveAura(execution: CommandExecution) {
        val player = execution.player
        val auraStr = execution.getArg(1)
        val profile = player.profile
        val auraType = EquipableManager.auras.getType(auraStr) ?: return
        val aura = auraType.newEquipable()
        profile.auras.add(aura)
    }

    fun givePet(execution: CommandExecution) {
        val player = execution.player
        val petStr = execution.getArg(1)
        val profile = player.profile
        val petType = EquipableManager.pets.getType(petStr) ?: return
        val pet = petType.newEquipable()
        profile.pets.add(pet)
    }


    fun trackedPlayers(execution: CommandExecution) {
        val player = execution.player
        val location = player.location
        val entities = location.getNearbyBukkitEntities(5.0)
        debug(entities.map { "${it.name} + ${it.entityId}" })
    }

    fun nearbyEntities(execution: CommandExecution) {
        val player = execution.player
        val location = player.location
        val entities = location.getNearbyBukkitEntities(5.0)
        debug(entities)
    }

    fun shopsList(execution: CommandExecution) {
        val location = execution.player.location
        debug(location.world.data.objectsManager.objectsTree.findNearSpecific(location, 5.0, Shop::class.java))
    }

    fun viewBuffs(execution: CommandExecution) {
        val player = execution.player
        val profile = player.profile
        val rpg = profile.rpg
        val buffs = rpg.buffs
        debug(buffs)
    }

    fun playerSpeedTest(execution: CommandExecution) {
        val player = execution.player
        val handle = player.nms
        val abilities = handle.abilities
        debug(abilities.walkSpeed)
        val speed = execution.getFloat(0)
        abilities.walkSpeed = speed
        handle.updateAbilities()
    }

    fun blockBreakProgress(execution: CommandExecution) {
        val player = execution.player
        player.setBlockBreakStage(130, 151, 142, 7)
    }

    fun findClasses(execution: CommandExecution) {
        val packageName = "org.ndk.godsimulator.rpg"
        debugExecTime {
            val classLoader = GodSimulator.instance.r_GetField("classLoader").result as ClassLoader
            debug(Tools.findClasses(classLoader, packageName))
        }
    }

    fun petsAmount(execution: CommandExecution) {
        val player = execution.player
        val profile = player.profile
        val pets = profile.pets
        debug(pets.all.size)
    }

    fun giveTestPet(execution: CommandExecution) {
        val player = execution.player
        val profile = player.profile
        val petType = EquipableManager.pets.getType("speedy") ?: return
        repeat(100) {
            val pet = petType.newEquipable()
            profile.pets.add(pet)
        }
    }

    fun configInheritance(execution: CommandExecution) {
        val config = ConfigsManager.load("test")

        val section = config.getSection("test")
        debug(section)
    }

    fun missingRuTranslations(execution: CommandExecution) {
        val all = MSG.entries
        val ru = languagesManager[Language.Code.RU_RU]!!
        var count = 0
        for (msg in all) {
            if (!ru.hasMessage(msg.id)) {
                debug(msg.id)
                count++
            }
        }

        if (count == 0) {
            debug("All translations are present")
        }
    }


    fun printObjects(execution: CommandExecution) {
        val world = execution.player.location.world.data
        for (region in world.objectsManager.objectsTree) {
            debug(region)
        }
    }



    fun objectsIn(execution: CommandExecution) {
        val player = execution.player
        val location = player.location
        val world = location.world.data
        debug(world.objectsManager.objectsTree.find(location))
    }

    fun nearbyBuildings(execution: CommandExecution) {
        debug(BuildingsManager.getBuildings(execution.player, execution.player.location, 5.0))
        debugAverageExecTime("Get nearby buildings", 1000) {
            BuildingsManager.getBuildings(execution.player, execution.player.location, 5.0)
        }
    }

    fun nearbyObjects(execution: CommandExecution) {
        val world = execution.player.location.world.data
        debug(world.objectsManager.objectsTree.findNearby(execution.player.location, 5.0))
    }

    fun forceSelectGod(execution: CommandExecution) {
        val player = execution.player
        val profile = player.profile
        profile.forceSelectGod()
    }

    fun asyncAreaSet(execution: CommandExecution) {
        val world = execution.player.world
        scheduler.runTaskAsynchronously {
            world.fillArea(
                Material.DIAMOND_BLOCK,
                127, 151, 145,
                127, 151, 145
            )
        }
    }

    fun guiList() {
        debug(GUIManager.guis)
    }

    fun linkedItem(execution: CommandExecution) {
        val player = execution.player
        val world = player.location.world
        val block = world.getBlockAt(130, 151, 148)
        val state = block.state as Chest
        val craftInventory = state.inventory as CraftInventory
        val firstItem = craftInventory.inventory.getItem(0)
        val inventory = player.nms.inventory
        inventory.setItem(0, firstItem)
        player.sendSimulatorMessage("Link saved: ${firstItem === inventory.getItem(0)}")
    }

    // Was commented because of using accessor on offline player, which is not allowed more from 28.03.2023
//    fun profileLoadTest() {
//        debugExecTime("Get bagsInventory") {
//            @Suppress("DEPRECATION")
//            val player = Bukkit.getOfflinePlayer("Nik_De_Kur")
//            val accessor = player.accessor
//            val profile = accessor.profile
//            profile.bagsInventory
//        }
//    }

    fun showWorld(execution: CommandExecution) {
        val world = (execution.sender as Player).location.world
        debug(world.uid, world.name)
    }

    fun spawnNPC(execution: CommandExecution) {
        NPCManager.spawn("test", (execution.sender as Player).location)
    }

    fun locationsCheck(execution: CommandExecution) {
        val player = execution.sender as Player
        val playerLocation = player.blockLocation
        val simulatorLocation = player.simulatorLocation
        debug(simulatorLocation?.id, simulatorLocation?.getSellZone(playerLocation)?.region)
    }

    @Suppress("UNUSED_PARAMETER")
    fun dbtest(execution: CommandExecution) {
        val core = Database.core

        val table = core.newTable("test_players_speed", "Id VARCHAR(36) PRIMARY KEY, Data TEXT")

        core.execute(Query.update("DROP INDEX IF EXISTS test_players_speed_index ON test_players_speed")).join()
        core.execute(Query.update("CREATE INDEX IF NOT EXISTS test_players_speed_index ON test_players_speed (Id)")).join()

        debugExecTime("Clearing table") {
            table.clearRows().join()
        }
        debug("cleared")

        val uuids = RealRange(1, 100_000).map { UUID.randomUUID() }

        val map = mapOf(
            "language" to "en",
            "profiles" to mapOf(
                1 to mapOf(
                    "balance" to "2250",
                    "unlockedLocations" to listOf("nether"),
                    "bag" to "infinity",
                    "god" to "zeus",
                    "level" to 5,
                    "exp" to 121,
                    "skills" to mapOf(
                        1 to mapOf(
                            "level" to 500,
                            "exp" to 2_000_000
                        )
                    )
                )
            )
        )

        // ConvertingToJSON
        debugExecTime("ConvertingToJSON") {
            uuids.associateWith { GSON.gson.toJson(map) }
        }
        val json = GSON.gson.toJson(map)


        // Add
        debugExecTime("Add") {
            table.addRows(uuids.map { listOf(it, json) }).join()
        }

        // Get
        val getTest = 100
        val futures = arrayOfNulls<CompletableFuture<*>>(getTest)
        val times = arrayOfNulls<Long>(getTest)
        val globalTimes: MutableList<Double> = ArrayList(getTest)
        repeat(50) {
            val lastUuids = uuids.chooseUnique(getTest)
            lastUuids.forEachIndexed { i, uuid ->
                val start = System.nanoTime()
                futures[i] = table
                    .select()
                    .column("Data")
                    .where("Id").isEqualTo(uuid.toString())
                    .limit(1)
                    .execute()
                    .thenAccept {
                        val end = System.nanoTime()
                        times[i] = end - start
                    }
            }
            CompletableFuture.allOf(*futures).join()
            globalTimes.add(times.notNull().average())
        }
        debug("Get: ${globalTimes.average().nanosToMs().format(2)} ms")

    }

    override val permissionNode: String = "cmd.test"
    override val isConsoleFriendly: Boolean = true
    override val argsRequirement: Int = 1

    override val usageMSG: MSGHolder = MSG.INTERNAL_ERROR

    override val name: String
        get() = "test"
}