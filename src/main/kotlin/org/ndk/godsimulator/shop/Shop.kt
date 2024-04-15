package org.ndk.godsimulator.shop

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.ndk.global.spatial.Point
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.godsimulator.wobject.Object
import org.ndk.godsimulator.wobject.WorldRegion
import org.ndk.klib.ceil
import org.ndk.klib.floor
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.extension.handle
import org.ndk.minecraft.extension.removeEntities
import org.ndk.minecraft.extension.spawnEntity
import org.ndk.minecraft.language.MSGHolder
import kotlin.properties.Delegates.notNull

class Shop(
    val shopId: String,
    val pattern: Pattern,
    override val location: Location,
) : Object(), WorldRegion, MSGNameHolder {

    override val hologramSpawnTranslation: Vector = pattern.hologramTranslation

    var entity by notNull<Entity>()
        private set
    val entityLocation: Location
        get() = entity.location
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

    override fun spawn() {
        location.removeEntities()
        entity = pattern.spawn(location)
        super.spawn()
    }

    override fun remove() {
        super.remove()
        entity.remove()
    }

    override fun getHologramText(player: Player): Collection<String> {
        return player.getLangMsg(pattern.msgName, getFinalPlaceholder(player)).listText
    }




    data class Pattern(
        val id: String,
        val msgName: MSGHolder,
        val entityType: EntityType,
        val hologramTranslation: Vector
    ) {

        fun spawn(location: Location): Entity {
            val entity = location.spawnEntity(entityType) ?: throw RuntimeException("Failed to spawn shop entity")
            if (entity is LivingEntity) {
                entity.setAI(false)
                entity.isCollidable = false
                entity.canPickupItems = false
            }
            entity.setGravity(false)
            entity.isInvulnerable = true
            entity.isSilent = true
            return entity
        }
    }

    override val defaultPhName: String
        get() = "shop"
    override val nameMSG = pattern.msgName
}