package org.ndk.godsimulator.wobject.entity

import net.minecraft.server.v1_12_R1.*
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSkeleton
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.util.Vector
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.item.ItemPattern

class EntitySkeletonImpl(world: World) : EntitySkeleton(world) {

    override fun r() {
        goalSelector.a(1, PathfinderGoalFloat(this))
        goalSelector.a(2, PathfinderGoalRestrictSun(this))
        goalSelector.a(3, PathfinderGoalFleeSun(this, 1.0))
        goalSelector.a(3, PathfinderGoalAvoidTarget(this, EntityWolf::class.java, 6.0f, 1.0, 1.2))
        goalSelector.a(5, PathfinderGoalRandomStrollLand(this, 1.0))
        goalSelector.a(6, PathfinderGoalLookAtPlayer(this, EntityHuman::class.java, 8.0f))
        goalSelector.a(6, PathfinderGoalRandomLookaround(this))
        targetSelector.a(1, PathfinderGoalHurtByTarget(this, false, *arrayOfNulls(0)))
        targetSelector.a(2, PathfinderGoalNearestAttackableTarget(this, EntityHuman::class.java, true))
        targetSelector.a(3, PathfinderGoalNearestAttackableTarget(this, EntityIronGolem::class.java, true))
    }

    // No sun burning
    override fun n() {}
}


class PatternOverworldSkeleton : MobPattern<EntitySkeletonImpl> {
    override val id: String = "OverworldSkeleton"
    override val defaultName: String = "Overworld Skeleton"
    override val hologramTranslation = Vector(0, 1, 0)
    override val clazz: Class<EntitySkeletonImpl> = EntitySkeletonImpl::class.java

    override fun createEntity(location: Location): EntitySkeletonImpl {
        val worldServer = location.worldHandle
        val entity = EntitySkeletonImpl(worldServer)
        EntityArmorStand(location.worldHandle)
        entity.setArmor(
            helmet = GREEN_HELMET,
            chestplate = GREEN_CHESTPLATE,
            leggings = GREEN_LEGGINGS,
            boots = GREEN_BOOTS
        )
        entity.setInvulnerable(true)
        entity.persistent = true
        entity.setLocation(location.x, location.y, location.z, location.yaw, location.pitch)
        entity.isNoGravity = true
        entity.removeEquipmentDrop()
        return entity
    }

    override fun postSpawn(player: Player, entity: EntitySkeletonImpl) {
        player.sendEntityArmor(entity.id, mapOf(
            EnumItemSlot.HEAD to GREEN_HELMET,
            EnumItemSlot.CHEST to GREEN_CHESTPLATE,
            EnumItemSlot.LEGS to GREEN_LEGGINGS,
            EnumItemSlot.FEET to GREEN_BOOTS
        ))
    }


    override fun spawnWorld(location: Location): CraftSkeleton {
        val entity = createEntity(location)
        return location.world.craft.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM)
    }
    companion object {
        val GREEN_HELMET = ItemPattern.from(Material.LEATHER_HELMET)
            .setArmorColor(Color.GREEN)
            .build(null)
            .nmsCopy()
        val GREEN_CHESTPLATE = ItemPattern.from(Material.LEATHER_CHESTPLATE)
            .setArmorColor(Color.GREEN)
            .build(null)
            .nmsCopy()
        val GREEN_LEGGINGS = ItemPattern.from(Material.LEATHER_LEGGINGS)
            .setArmorColor(Color.GREEN)
            .build(null)
            .nmsCopy()
        val GREEN_BOOTS = ItemPattern.from(Material.LEATHER_BOOTS)
            .setArmorColor(Color.GREEN)
            .build(null)
            .nmsCopy()
    }
}