@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.wobject.entity


import net.minecraft.server.v1_12_R1.EntityTypes
import org.bukkit.Location
import org.bukkit.entity.Player
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.extension.readMSGOrThrow
import org.ndk.godsimulator.world.WorldsManager.Companion.data
import org.ndk.klib.withUnlock
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.plugin.ServerPlugin
import java.math.BigInteger
import java.util.*
import org.ndk.minecraft.extension.getListSection as getListSection1


class EntitiesManager : PluginModule {
    override val id: String = "EntitiesManager"

    val patternsMobs = HashMap<String, MobPattern<*>>()
    val patternsEntities = HashMap<String, EntityPattern<*>>()
    val entities = HashMap<UUID, Entity<*>>()

    init {
        addMobPattern(51, PatternOverworldSkeleton())
    }

    override fun onLoad(plugin: ServerPlugin) {

        val patternsConfig = plugin.configsManager.load("entities_patterns")
        for (patternS in patternsConfig.getListSection1()) {
            try {
                val id = patternS.name
                val nameMSG = patternS.readMSGOrThrow("name")
                val mobId = patternS.getStringOrThrow("mob")
                val mobPattern = getMobPattern(mobId) ?: throw NoSuchElementException("MobPattern '$mobId' not found")
                val hologramTranslation = patternS.readVectorOrThrow("hologramTranslation")
                addEntityPattern(EntityPattern(id, nameMSG, mobPattern, hologramTranslation))
            } catch (e: Exception) {
                plugin.logger.warning("Error while loading entity pattern: ${patternS.name}")
                e.printStackTrace()
                continue
            }
        }

        val worlds = GodSimulator.worlds
        val config = plugin.configsManager.load("entities")
        for (entityS in config.getListSection1()) {
            try {
                val pattern = entityS.getStringOrThrow("pattern")
                val entityPattern = getEntityPattern(pattern) ?: throw NoSuchElementException("Pattern '$pattern' not found")
                val respawnTicks = entityS.getLongOrThrow("respawnTicks")
                val level = entityS.getIntOrThrow("level")
                val maxHealth = entityS.readBigIntegerOrThrow("maxHealth")
                val expDrop = entityS.readBigInteger("expDrop", BigInteger.ZERO)!!
                val bagFill = entityS.readBigInteger("bagFill", BigInteger.ZERO)!!
                val soulDrop = entityS.readBigInteger("soulDrop", BigInteger.ZERO)!!

                for (world in worlds) {
                    val spawnpoint = entityS.readLocationOrThrow("spawnpoint", world = world.bukkit)
                    addEntity(entityPattern, respawnTicks, spawnpoint, level, maxHealth, expDrop, bagFill, soulDrop)
                }
            } catch (e: Exception) {
                plugin.logger.warning("Error while loading entity: ${entityS.name}")
                e.printStackTrace()
                continue
            }
        }
        GodSimulator.entitiesManager = this
    }

    override fun onUnload(plugin: ServerPlugin) {
        entities.clear()
        patternsEntities.clear()
        GodSimulator.worlds.forEach {
            it.objectsManager.removeEntities()
        }
    }

    fun addMobPattern(id: Int, mobPattern: MobPattern<*>) {
        patternsMobs[mobPattern.id] = mobPattern
        registerEntityType(id, mobPattern)
    }
    fun getMobPattern(id: String): MobPattern<*>? {
        return patternsMobs[id]
    }

    fun addEntityPattern(entityPattern: EntityPattern<*>) {
        patternsEntities[entityPattern.id] = entityPattern
    }
    fun getEntityPattern(id: String): EntityPattern<*>? {
        return patternsEntities[id]
    }

    fun addEntity(entity: Entity<*>) {
        entity.spawn()
        entities[entity.id] = entity
        entity.world.objectsManager.register(entity)
    }

    fun addEntity(
        pattern: EntityPattern<*>,
        respawnTicks: Long,
        location: Location,
        level: Int,
        maxHealth: BigInteger,
        expDrop: BigInteger,
        bagFill: BigInteger,
        soulDrop: BigInteger,
    ): Entity<*> {
        val entity = Entity(pattern, respawnTicks, location, level, maxHealth, expDrop, bagFill, soulDrop)
        addEntity(entity)
        return entity
    }

    inline fun getEntities(player: Player, x: Int, y: Int, z: Int, radius: Double = 0.0): Set<Entity<*>> {
        val world = player.world.data
        val entities = if (radius <= 0.0) {
            world.objectsManager.findEntities(x, y, z)
        } else {
            world.objectsManager.findNearEntities(x, y, z, radius)
        }

        entities.removeIf { it.isKilled }
        return entities
    }

    inline fun getEntities(player: Player, location: Location, radius: Double = 0.0): Set<Entity<*>> {
        return getEntities(player, location.blockX, location.blockY, location.blockZ, radius)
    }


    companion object {
        @JvmStatic
        fun registerEntityType(id: Int, mobPattern: MobPattern<*>) {
            val method = EntityTypes::class.java.getDeclaredMethod(
                "a",
                Int::class.javaPrimitiveType,
                String::class.java,
                Class::class.java,
                String::class.java
            )
            method.withUnlock {
                method.invoke(null, id, mobPattern.id, mobPattern.clazz, mobPattern.defaultName)
            }
        }
    }
}