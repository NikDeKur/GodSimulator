package org.ndk.godsimulator.wobject.entity

import net.minecraft.server.v1_12_R1.DamageSource
import net.minecraft.server.v1_12_R1.EntityLiving
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.reward.BagFillReward
import org.ndk.godsimulator.reward.ExperienceReward
import org.ndk.godsimulator.reward.SoulsReward
import org.ndk.godsimulator.wobject.LootableLivingObject
import org.ndk.klib.r_CallMethodTyped
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.extension.nms
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


    override fun getHologramText(player: Player): Collection<String> {
        return player.getLangMsg(MSG.ENTITY_HOLOGRAM_TEXT, getFinalPlaceholder(player)).listText
    }

    fun playDamageSound(source: DamageSource) {
        entity.nms.r_CallMethodTyped("c", DAMAGE_SOURCE_ARRAY, source)
    }

    override fun damage(player: Player, damage: BigInteger) {
        super.damage(player, damage)

        // Play damage sound
        val nmsPlayer = player.nms
        val source = DamageSource.playerAttack(nmsPlayer)
        playDamageSound(source)
    }


    override fun spawn() {
        location.removeEntities()
        entity = pattern.mob.spawnWorld(location)
        entity.health = 1.0
        EntitiesManager.addBukkitEntity(entity, this)
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

        EntitiesManager.removeBukkitEntity(entity)
    }


    companion object {
        val DAMAGE_SOURCE_ARRAY = arrayOf(DamageSource::class.java)
    }
}