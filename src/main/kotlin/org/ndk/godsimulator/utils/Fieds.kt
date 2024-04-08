package org.ndk.godsimulator.utils

import com.google.gson.TypeAdapter
import org.ndk.godsimulator.equipable.inventory.EquipableInventory
import org.ndk.godsimulator.equipable.type.EquipableType
import org.ndk.godsimulator.equipable.type.EquipableTypesManager
import org.ndk.godsimulator.profile.PlayerProfile
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * Represents a field for smart data storing.
 *
 * When value is accessed first, it will be deserialised by [deserializer] and stored in scopes instead of the stored primitive value.
 *
 * When the value is set, it will store in scopes.
 *
 * Adding a new field type requires you to add Gson [TypeAdapter] for the type to be converted from high level to primitive value on save time.
 *
 * This specific mechanism allows to setting values in a map directly in primitive times, and deserialise them on access.
 */
open class ClassDataHolderField<T : Any>(
    val profile: PlayerProfile,
    val path: String,
    val default: Any,
    val deserializer: (Any) -> T
) : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val deserialize = deserializer(profile.scopes.accessor[path] ?: default)
        profile.scopes.accessor[path] = deserialize
        return deserialize
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        profile.scopes.accessor[path] = value
    }
}

@Suppress("UNCHECKED_CAST")
class InventoryHolderField<T : EquipableType<T>, CLZ : EquipableInventory<T>>(
    profile: PlayerProfile,
    path: String,
    val inventoryClazz: Class<CLZ>,
    val manager: EquipableTypesManager<T>
) : ClassDataHolderField<CLZ>(
    profile,
    path,
    "{}",
    {
        if (inventoryClazz.isInstance(it)) {
            it as CLZ
        } else if (it is String) {
            EquipableInventory.fromSerialized(profile, manager::deserialize, inventoryClazz, it)
        } else {
            throw IllegalArgumentException("Invalid inventory type")
        }
    }
)
