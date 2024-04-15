package org.ndk.godsimulator.wobject.entity

import net.minecraft.server.v1_12_R1.*
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.util.Vector
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.item.ItemPattern

class EntitySkeletonImpl(world: World) : EntitySkeleton(world) {

    override fun move(enummovetype: EnumMoveType, d0: Double, d1: Double, d2: Double) {}

    public override fun r() {
        goalSelector.a(1, PathfinderGoalFloat(this))
        goalSelector.a(6, PathfinderGoalLookAtPlayer(this, EntityHuman::class.java, 8.0f))
        goalSelector.a(6, PathfinderGoalRandomLookaround(this))

        targetSelector.a(1, PathfinderGoalHurtByTarget(this, false))
        targetSelector.a(2, PathfinderGoalNearestAttackableTarget(this, EntityHuman::class.java, true))
    }

}


class PatternOverworldSkeleton : MobPattern<EntitySkeletonImpl> {
    override val id: String = "OverworldSkeleton"
    override val defaultName: String = "Overworld Skeleton"
    override val hologramTranslation = Vector(0, 1, 0)
    override val clazz: Class<EntitySkeletonImpl> = EntitySkeletonImpl::class.java

    override fun createEntity(location: Location): EntitySkeletonImpl {
        val worldServer = location.worldHandle
        val entity = EntitySkeletonImpl(worldServer)
        entity.setLocation(location.x, location.y, location.z, location.yaw, location.pitch)
        entity.setArmor(
            helmet = GREEN_HELMET,
            chestplate = GREEN_CHESTPLATE,
            leggings = GREEN_LEGGINGS,
            boots = GREEN_BOOTS
        )
        entity.setInvulnerable(true)
        entity.persistent = true

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


    override fun spawnWorld(location: Location): LivingEntity {
        val entity = createEntity(location)
        return location.world.craft.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM) as LivingEntity
    }

    companion object {
        val GREEN_HELMET = ItemPattern.from(Material.LEATHER_HELMET)
            .setArmorColor(Color.GREEN)
            .setUnbreakable(true)
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