package org.ndk.godsimulator.wobject.entity

import net.minecraft.server.v1_12_R1.EntityLiving
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.ndk.minecraft.extension.spawnEntities

interface MobPattern<T : EntityLiving> {

    val id: String
    val defaultName: String
    val hologramTranslation: Vector

    val clazz: Class<T>

    fun createEntity(location: Location): T
    fun postSpawn(player: Player, entity: T) {}

    fun spawnWorld(location: Location): LivingEntity

    fun spawnPlayer(player: Player, location: Location): T {
        val entity = createEntity(location)
        player.spawnEntities(entity)
        return entity
    }
}