@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.world

import net.minecraft.server.v1_12_R1.Packet
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.ndk.godsimulator.wobject.ObjectsManager
import org.ndk.minecraft.extension.handle
import org.ndk.minecraft.extension.sendPackets
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// Note: Cannot implement Bukkit World by world because bukkit wants it to be CraftWorld
class SimulatorWorld(
    val bukkit: World
) : HashMap<String, Any>() {

    val nms = bukkit.handle

    val players: List<Player>
        get() = bukkit.players
    val objectsManager = ObjectsManager(this)

    inline fun throwNotProvided(key: String): Nothing {
        throw IllegalArgumentException("Key $key is not provided for world ${bukkit.name}!")
    }

    var spawnLocation by object : ReadWriteProperty<Any, Location> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Location {
            return this@SimulatorWorld["spawnLocation"] as? Location ?: throwNotProvided("spawnLocation")
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Location) {
            this@SimulatorWorld["spawnLocation"] = value
        }
    }

    inline fun forEachPlayer(action: (Player) -> Unit) {
        players.forEach(action)
    }

    fun broadcast(packet: List<Packet<*>>) {
        forEachPlayer {
            it.sendPackets(packet)
        }
    }
    inline fun broadcast(vararg packets: Packet<*>) {
        return broadcast(packets.toList())
    }
}
