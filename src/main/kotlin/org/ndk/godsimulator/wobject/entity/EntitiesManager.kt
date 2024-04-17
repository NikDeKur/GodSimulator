package org.ndk.godsimulator.wobject.entity


import net.minecraft.server.v1_12_R1.EntityTypes
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.ndk.godsimulator.GodSimulator
import org.ndk.klib.withUnlock
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.plugin.ServerPlugin
import java.math.BigInteger
import java.util.*


object EntitiesManager : PluginModule {
    override val id: String = "EntitiesManager"

    val patternsMobs = HashMap<String, MobPattern<*>>()
    val patternsEntities = HashMap<String, EntityPattern<*>>()

    /**
     * Entity Object UUID to Entity Object
     */
    val entities = HashMap<UUID, Entity<*>>()

    /**
     * Entity UUID to Entity Object
     *
     * If you want to get simulator entity from bukkit entity, use this map.
     *
     * Simulator object entity cannot have the same id as bukkit entity, because bukkit entity can be spawned/removed.
     */
    val entitiesByBukkit = HashMap<UUID, Entity<*>>()

    override fun onLoad(plugin: ServerPlugin) {

        // Register mob patterns
        addMobPattern(51, PatternOverworldSkeleton())


        // Register entity patterns
        val patternsConfig = plugin.configsManager.load("entities_patterns")
        patternsConfig.forEachSectionSafe {
            val id = it.name
            val nameMSG = it.readMSGHolderOrThrow("name")
            val mobId = it.getStringOrThrow("mob")
            val mobPattern = getMobPattern(mobId) ?: throw NoSuchElementException("MobPattern '$mobId' not found")
            val hologramTranslation = it.readVectorOrThrow("hologramTranslation")
            addEntityPattern(EntityPattern(id, nameMSG, mobPattern, hologramTranslation))
        }

        val worlds = GodSimulator.worlds
        val config = plugin.configsManager.load("entities")
        config.forEachSectionSafe {
            val pattern = it.getStringOrThrow("pattern")
            val entityPattern = getEntityPattern(pattern) ?: throw NoSuchElementException("Pattern '$pattern' not found")
            val respawnTicks = it.getLongOrThrow("respawnTicks")
            val level = it.getIntOrThrow("level")
            val maxHealth = it.readBigIntegerOrThrow("maxHealth")
            val expDrop = it.readBigInteger("expDrop", BigInteger.ZERO)!!
            val bagFill = it.readBigInteger("bagFill", BigInteger.ZERO)!!
            val soulDrop = it.readBigInteger("soulDrop", BigInteger.ZERO)!!

            for (world in worlds) {
                val spawnpoint = it.readLocationOrThrow("spawnpoint", defWorld = world.bukkit)
                addEntity(entityPattern, respawnTicks, spawnpoint, level, maxHealth, expDrop, bagFill, soulDrop)
            }
        }
    }

    override fun onUnload(plugin: ServerPlugin) {
        entities.clear()
        entitiesByBukkit.clear()
        patternsEntities.clear()
        GodSimulator.worlds.forEach {
            val manager = it.objectsManager
            entities.values.forEach(manager::unregister)
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


    fun addBukkitEntity(bukkit: LivingEntity, entity: Entity<*>) {
        entitiesByBukkit[bukkit.uniqueId] = entity
    }

    fun removeBukkitEntity(entity: LivingEntity) {
        entitiesByBukkit.remove(entity.uniqueId)
    }

    fun getEntityByBukkitId(id: UUID): Entity<*>? {
        return entitiesByBukkit[id]
    }


    fun getEntities(player: Player, location: Location, radius: Double = 0.0): List<Entity<*>> {
        return location.getNearbyBukkitEntities(radius)
            .mapNotNull {
                val entity = getEntityByBukkitId(it.uniqueId)
                if (entity != null && entity.isKilled) return@mapNotNull null
                entity
            }
    }


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