package org.ndk.godsimulator.equipable.inventory

import org.ndk.godsimulator.database.Database
import org.ndk.godsimulator.equipable.Equipable
import org.ndk.godsimulator.equipable.type.EquipableType
import org.ndk.godsimulator.event.equipable.EquipableEquipEvent
import org.ndk.godsimulator.event.equipable.EquipableUnEquipEvent
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.klib.*
import org.ndk.minecraft.plugin.ServerPlugin.Companion.callEvent
import java.util.*

/**
 * Abstract Class for profile inventories, that store some objects, that can be equipped
 */
abstract class EquipableInventory<T : EquipableType<T>>(val profile: PlayerProfile) {

    val all = LinkedHashMap<UUID, Equipable<T>>()
    @Transient
    val allTypesSet = HashSet<String>()

    val equipped = LinkedHashMap<UUID, Equipable<T>>()
    @Transient
    val equippedTypesSet = HashSet<String>()

    /**
     * Limit of equipped objects
     * 
     * Set -1 to disable limit
     */
    abstract val equipLimit: Int

    /**
     * Limit of all either equipped or not objects in inventory
     * 
     * Set -1 to disable limit
     */
    abstract val allLimit: Int

    /**
     * Check if the limit of all objects is exceeded
     *
     * @return true if the limit is exceeded
     */
    val limitExceedAll: Boolean
        get() = allLimit != -1 && all.size >= allLimit

    /**
     * Check if the limit of equipped objects is exceeded
     *
     * @return true if the limit is exceeded
     */
    val limitExceedEquip: Boolean
        get() = equipLimit != -1 && equipped.size >= equipLimit

    open fun add(obj: Equipable<T>): Boolean {
        if (limitExceedAll) return false
        all.addById(obj)
        allTypesSet.add(obj.type.id)
        return true
    }

    open fun addAll(objs: Iterable<Equipable<T>>) {
        objs.forEach {
            add(it)
        }
    }

    /**
     * Remove the object from the inventory
     *
     * If the inventory does not contain the object, return false
     *
     * If the object is equipped, it will be unequipped
     *
     * @param obj the object to remove
     * @return true if the object was removed
     * @see has
     * @see unEquip
     */
    open fun remove(obj: Equipable<T>): Boolean {
        if (!has(obj)) return false
        all.removeById(obj)

        if (all.none { it.value.type.id == obj.type.id })
            allTypesSet.remove(obj.type.id)

        unEquip(obj)
        return true
    }

    open fun remove(uuid: UUID): Boolean {
        val obj = get(uuid) ?: return false
        return remove(obj)
    }

    open fun has(pet: Equipable<T>) = all.containsById(pet)
    open fun has(uuid: UUID) = all.containsKey(uuid)
    open fun has(type: T) = has(type.id)
    open fun has(type: String) = allTypesSet.contains(type)


    open fun get(uuid: UUID) = all[uuid]

    open fun get(type: T): Iterable<Equipable<T>> {
        return all.values.filter { it.type.id == type.id }
    }

    /**
     * Equip the object and return true if the object was equipped
     *
     * If equipped limit is exceeded, return false
     *
     * If the object is already equipped, return false
     *
     * If the object is not in the all-inventory, return false
     *
     * Call [EquipableEquipEvent]
     *
     * @param obj object to equip
     * @return true if the object was equipped
     */
    open fun equip(obj: Equipable<T>): Boolean {
        if (limitExceedEquip) return false
        if (isEquipped(obj)) return false
        if (!has(obj)) return false

        val event = EquipableEquipEvent(this, obj)
        callEvent(event)
        if (event.isCancelled) return false

        equipped.addById(obj)
        equippedTypesSet.add(obj.type.id)
        obj.onEquip(this)
        return true
    }

    open fun equip(uuid: UUID): Boolean {
        val pet = all[uuid] ?: return false
        return equip(pet)
    }

    open fun unEquip(obj: Equipable<T>): Boolean {
        if (!isEquipped(obj)) return false

        val event = EquipableUnEquipEvent(this, obj)
        callEvent(event)
        if (event.isCancelled) return false

        equipped.removeById(obj)
        if (equipped.none { it.value.type.id == obj.type.id })
            equippedTypesSet.remove(obj.type.id)
        obj.onUnEquip(this)
        return true
    }

    open fun unEquip(uuid: UUID): Boolean {
        val pet = get(uuid) ?: return false
        return unEquip(pet)
    }

    open fun unEquipAll() {
        for (value in equipped.values) {
            unEquip(value)
        }
        equipped.clear()
        equippedTypesSet.clear()
    }

    open fun isEquipped(obj: Equipable<T>) = equipped.containsById(obj)
    open fun isEquipped(uuid: UUID) = equipped.containsKey(uuid)
    open fun isEquipped(type: T) = isEquipped(type.id)
    open fun isEquipped(type: String) = equippedTypesSet.contains(type)

    open fun isEmpty() = all.isEmpty()
    open fun isNotEmpty() = all.isNotEmpty()
    open fun isEquippedEmpty() = equipped.isEmpty()

    open fun clear() {
        unEquipAll()
        all.clear()
        allTypesSet.clear()
    }

    open fun serialize(): String {
        val all = all.map { it.value.serialize() }
        val equipped = equipped.map { it.value.id.toString() }
        if (all.isEmpty() && equipped.isEmpty()) return "{}"
        if (equipped.isEmpty()) return """{all: $all}"""
        return """{all: $all, equipped: $equipped}"""
    }

    companion object {
        fun <T : EquipableType<T>, CLZ : EquipableInventory<T>> fromSerialized(
            profile: PlayerProfile,
            deSerializer: (String) -> Equipable<T>?,
            clazz: Class<CLZ>,
            serialized: String
        ): CLZ {
            val map = Database.GSON.fromJson<Map<String, Collection<String>>>(serialized, HashMap::class.java)

            val inventory = clazz.construct(profile)

            val all = map["all"]?.mapNotNull(deSerializer)
            if (all != null)
                inventory.addAll(all)

            map["equipped"]
                ?.mapNotNull(String::uuid)
                ?.forEach(inventory::equip)

            return inventory
        }
    }
}