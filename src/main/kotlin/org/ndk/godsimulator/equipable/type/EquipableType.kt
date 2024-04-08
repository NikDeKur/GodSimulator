package org.ndk.godsimulator.equipable.type

import org.ndk.global.interfaces.Snowflake
import org.ndk.godsimulator.equipable.Equipable
import org.ndk.godsimulator.language.MSGNameHolder
import java.util.*

interface EquipableType<T : EquipableType<T>> : MSGNameHolder, Snowflake<String> {
    val manager: EquipableTypesManager<T>
    val hierarchy: Int

    override val defaultPhName
        get() = manager.qualifier

    fun newEquipable(id: UUID): Equipable<T>
    fun newEquipable(): Equipable<T> {
        return newEquipable(UUID.randomUUID())
    }
}