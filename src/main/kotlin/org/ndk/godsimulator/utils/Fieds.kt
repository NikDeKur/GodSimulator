package org.ndk.godsimulator.utils

import org.ndk.godsimulator.equipable.inventory.EquipableInventory
import org.ndk.godsimulator.equipable.type.EquipableType
import org.ndk.godsimulator.equipable.type.EquipableTypesManager
import org.ndk.godsimulator.profile.PlayerProfile


open class ClassDataHolderField<T : Any>(
    val profile: PlayerProfile,
    path: String,
    default: Any,
    type: Class<T>,
    deserializer: (Any) -> T
) : org.ndk.klib.ClassDataHolderField<String, Any, T>(profile.accessor, path, default, type, deserializer)

open class NotNullClassDataHolderField<T : Any>(
    val profile: PlayerProfile,
    path: String,
    default: Any,
    type: Class<T>,
    deserializer: (Any) -> T
) : org.ndk.klib.NotNullClassDataHolderField<String, Any, T>(profile.accessor, path, default, type, deserializer)


class InventoryHolderField<T : EquipableType<T>, CLZ : EquipableInventory<T>>(
    profile: PlayerProfile,
    path: String,
    val inventoryClazz: Class<CLZ>,
    val manager: EquipableTypesManager<T>
) : NotNullClassDataHolderField<CLZ>(
    profile,
    path,
    "{}",
    inventoryClazz,
    {
        if (it is String) {
            EquipableInventory.fromSerialized(profile, manager::deserialize, inventoryClazz, it)
        } else {
            throw IllegalArgumentException("Invalid inventory type")
        }
    }
)
