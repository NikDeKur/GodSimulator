package org.ndk.godsimulator.wobject

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.ndk.godsimulator.database.Database.Companion.accessorAsync
import org.ndk.godsimulator.event.wobject.LivingObjectDamageEvent
import org.ndk.godsimulator.event.wobject.LivingObjectKillEvent
import org.ndk.godsimulator.event.wobject.LivingObjectSpawnEvent
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profile
import org.ndk.godsimulator.wobject.damage.DamageMap
import org.ndk.minecraft.Utils.debug
import org.ndk.minecraft.plugin.ServerPlugin
import java.math.BigInteger


abstract class LivingObject : Object() {

    abstract val maxHealth: BigInteger


    open val damageMap by lazy {
        DamageMap(maxHealth, this::kill)
    }

    open val health: BigInteger
        get() = damageMap.left
    open val isKilled: Boolean
        get() = health <= BigInteger.ZERO


    override fun spawn() {
        val event = LivingObjectSpawnEvent(this)
        ServerPlugin.callEvent(event)
        if (event.isCancelled) return

        damageMap.clear()
        super.spawn()
    }

    open fun damage(player: Player, damage: BigInteger) {
        if (isKilled) return

        val event = LivingObjectDamageEvent(this, player, damage)
        ServerPlugin.callEvent(event)
        if (event.isCancelled) return

        player.profile.rpg.combatTracker.enterCombat(this)
        
        damageMap.register(player.profile.reference, damage)
        updateHologram()
    }

    open fun kill(killer: PlayerProfile.Reference) {
        val killerP = Bukkit.getOfflinePlayer(killer.playerId)

        killerP
            .accessorAsync
            .thenAccept {
                it.profile.rpg.combatTracker.leaveCombat(this)
            }

        val event = LivingObjectKillEvent(this, killerP)
        Bukkit.getPluginManager().callEvent(event)

        remove()
    }

    override fun toString(): String {
        return "LivingObject(id=$id, location=$location, health=$health/$maxHealth)"
    }
}