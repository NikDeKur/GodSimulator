package org.ndk.godsimulator.wobject.entity

import net.minecraft.server.v1_12_R1.EntityLiving
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.ndk.global.spatial.Point
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.reward.BagFillReward
import org.ndk.godsimulator.reward.ExperienceReward
import org.ndk.godsimulator.reward.SoulsReward
import org.ndk.godsimulator.wobject.LootableLivingObject
import org.ndk.klib.ceil
import org.ndk.klib.floor
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.extension.handle
import org.ndk.minecraft.extension.removeEntities
import java.math.BigInteger


class Entity<T : EntityLiving>(
    val pattern: EntityPattern<T>,
    override val respawnTicks: Long,
    override val location: Location,
    val level: Int,
    override val maxHealth: BigInteger,
    val expDrop: BigInteger,
    val bagFill: BigInteger,
    val soulDrop: BigInteger,
) : LootableLivingObject() {

    init {
        reward.addReward(ExperienceReward(expDrop))
        reward.addReward(BagFillReward(bagFill))
        reward.addReward(SoulsReward(soulDrop))
    }

    override val defaultPhName: String = "entity"
    override val nameMSG = pattern.nameMSG

    override val hologramSpawnTranslation = pattern.hologramTranslation

    private var _entity: LivingEntity? = null
    var entity: LivingEntity
        get() = _entity ?: throw IllegalStateException("Entity is not spawned yet!")
        set(value) {
            _entity = value
        }

    val entityBB
        get() = entity.handle.boundingBox!!

    override val minPoint
        get() = Point(
            (entityBB.a - 1).floor().toInt(),
            (entityBB.b - 1).floor().toInt(),
            (entityBB.c - 1).floor().toInt()
        )

    override val maxPoint
        get() = Point(
            (entityBB.d + 1).ceil().toInt(),
            (entityBB.e + 1).ceil().toInt(),
            (entityBB.f + 1).ceil().toInt()
        )

    override fun contains(x: Int, y: Int, z: Int): Boolean {
        val bb = entityBB
        return bb.a <= x && x <= bb.d &&
                bb.b <= y && y <= bb.e &&
                bb.c <= z && z <= bb.f
    }

    override fun getHologramText(player: Player): Collection<String> {
        return player.getLangMsg(MSG.ENTITY_HOLOGRAM_TEXT, getFinalPlaceholder(player)).listText
    }


    override fun spawn() {
        location.removeEntities()
        entity = pattern.mob.spawnWorld(location)
        entity.health = 1.0
        super.spawn()
    }

    override fun kill(killer: PlayerProfile.Reference) {
        super.kill(killer)
        entity.health = 0.0
    }

    override fun remove() {
        super.remove()

        // If entity already killed, we don't need to kill it again
        if (entity.health == 0.0) return

        // Get the chunk entity is in. NMS mechanisms would automatically load the chunk if it's not loaded
        entity.location.chunk

        // Get the actual entity object, if not found - entity either already killed or my theory about chunk loading is wrong. :(
        _entity = Bukkit.getEntity(entity.uniqueId) as? LivingEntity ?: return
        entity.health = 0.0
    }
}


//class Entity<T : EntityLiving>(
//    val pattern: EntityPattern<T>,
//    override val respawnTicks: Long,
//    override val location: Location,
//    val level: Int,
//    override val maxHealth: BigInteger,
//    override val expDrop: BigInteger,
//    override val bagFill: BigInteger
//) : LootableLivingObject() {
//
//    override val defaultPhName: String = "entity"
//    override val nameMSG: MSG = pattern.nameMSG
//
//    override val hologramSpawnTranslation: Vector = pattern.hologramTranslation
//
//    private var _entity: T? = null
//    var entity: T
//        get() = _entity ?: throw IllegalStateException("Entity is not spawned yet!")
//        set(value) {
//            _entity = value
//        }
//
//    val entityBB
//        get() = entity.boundingBox!!
//
//    override val minPoint
//        get() = Point(
//            (entityBB.a - 1).floor().toInt(),
//            (entityBB.b - 1).floor().toInt(),
//            (entityBB.c - 1).floor().toInt()
//        )
//
//    override val maxPoint
//        get() = Point(
//            (entityBB.d + 1).ceil().toInt(),
//            (entityBB.e + 1).ceil().toInt(),
//            (entityBB.f + 1).ceil().toInt()
//        )
//
//    override fun contains(x: Int, y: Int, z: Int): Boolean {
//        val bb = entityBB
//        return bb.a <= x && x <= bb.d &&
//                bb.b <= y && y <= bb.e &&
//                bb.c <= z && z <= bb.f
//    }
//
//    override fun getHologramText(player: Player): Collection<String> {
//        return player.getLangMsg(MSG.ENTITY_HOLOGRAM_TEXT, getFinalPlaceholder(player)).listText
//    }
//
//
//
//
//    override fun spawn() {
//        entity = pattern.mob.createEntity(location)
//        entity.health = 20f
//        super.spawn()
//    }
//
//    override fun show(player: Player) {
//        super.show(player)
//        player.spawnEntities(entity)
//        pattern.mob.postSpawn(player, entity)
//    }
//
//    override fun hide(player: Player) {
//        super.hide(player)
//        player.removeEntities(entity.id)
//    }
//
//    override fun remove() {
//        super.remove()
//        entity.health = 0f
//        world.broadcast(PacketPlayOutEntityStatus(entity, 3))
//
//        // If the plugin is not disabling, we can run the task via scheduler to create the animation
//        // If not we cannot use scheduler, so only hide the entity
//        if (GodSimulator.instance.isEnabled) {
//            GodSimulator.scheduler.runTaskLater(20L) {
//                hideForEveryone()
//            }
//        } else {
//            hideForEveryone()
//        }
//    }
//}
