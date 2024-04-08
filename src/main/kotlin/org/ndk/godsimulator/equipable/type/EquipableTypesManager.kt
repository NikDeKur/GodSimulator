package org.ndk.godsimulator.equipable.type

import org.ndk.godsimulator.GodSimulator.Companion.logger
import org.ndk.godsimulator.equipable.Equipable
import org.ndk.godsimulator.equipable.inventory.EquipableInventory
import org.ndk.godsimulator.language.MSGHolder
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.minecraft.plugin.ServerPlugin
import java.util.*

abstract class EquipableTypesManager<T : EquipableType<T>> {

    /**
     * Qualifier of the type
     *
     * Example: "aura", "bag", "pet"
     *
     * Used in serialisation, [MSGHolder.defaultPhName]
     */
    abstract val qualifier: String
    var isLoaded: Boolean = false
    val types = HashMap<String, T>()

    fun getType(id: String): T? {
        return types[id]
    }

    fun addType(type: T) {
        types[type.id] = type
    }

    fun load(plugin: ServerPlugin) {
        onLoad(plugin)
        isLoaded = true
    }

    abstract fun onLoad(plugin: ServerPlugin)
    fun unload() {
        types.clear()
        isLoaded = false
    }

    abstract fun getInventory(profile: PlayerProfile): EquipableInventory<T>


    fun deserialize(serialized: String): Equipable<T>? {
        // First is human-readable type
        // Example: "aura-speed-uuid"
        try {
            val (_, typeStr, id) = serialized.split("-", limit = 3)
            val type = getType(typeStr) ?: return null
            return type.newEquipable(UUID.fromString(id))
        } catch (e: Exception) {
            logger.warning("Failed to deserialize equipable: $serialized")
            e.printStackTrace()
            return null
        }
    }
}