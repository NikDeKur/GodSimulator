@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.database

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.ndk.godsimulator.equipable.inventory.EquipableInventory
import org.ndk.godsimulator.language.LangManager
import org.ndk.godsimulator.profile.ProfileLocations
import org.ndk.godsimulator.profile.ProfileQuests
import org.ndk.minecraft.Utils.debug
import org.ndk.minecraft.language.Language
import org.ndk.minecraft.language.MSGHolder
import java.math.BigInteger
import java.util.*


@Suppress("OVERRIDE_BY_INLINE")
class EasyTypeAdapter<T>(
    val clazz: Class<T>,
    val writeF: (JsonWriter, T) -> Unit,
    val readF: (JsonReader) -> T?
) : TypeAdapter<T>() {
    override inline fun write(out: JsonWriter, value: T) = writeF(out, value)
    override inline fun read(`in`: JsonReader) = readF(`in`)
    override fun toString() = "EasyTypeAdapter($clazz)"
}

inline fun <reified T> typeAdapter(
    noinline write: (JsonWriter, T) -> Unit,
    noinline read: (JsonReader) -> T?
) = EasyTypeAdapter(T::class.java, write, read)

fun GsonBuilder.register(adapter: EasyTypeAdapter<*>): GsonBuilder = this.registerTypeHierarchyAdapter(adapter.clazz, adapter)

fun JsonWriter.array(value: Iterable<String>) {
    beginArray()
    value.forEach { value(it) }
    endArray()
}


object GSON {

    val BigIntegerTypeAdapter = typeAdapter(
        { out, value -> out.value(value.toString()) },
        { BigInteger(it.nextString()) }
    )
    val UUIDTypeAdapter = typeAdapter<UUID>(
        { out, value -> out.value(value.toString()) },
        { UUID.fromString(it.nextString()) }
    )
    val LanguageCodeTypeAdapter = typeAdapter(
        { out, value -> out.value(value.code) },
        { Language.Code.fromCode(it.nextString()) }
    )
    val EquipableInventoryTypeAdapter = typeAdapter<EquipableInventory<*>>(
        { out, value -> if (value.isNotEmpty()) out.value(value.serialize()) else out.nullValue() },
        { throw UnsupportedOperationException("Can't deserialize EquipableInventory. Use Fields.InventoryHolderField") }
    )
    val ProfileLocationsTypeAdapter = typeAdapter<ProfileLocations>(
        { out, value -> out.array(value.serialize()) },
        { throw UnsupportedOperationException("Can't deserialize ProfileLocations. Use Fields.ClassDataHolderField")}
    )
    val QuestsTypeAdapter = typeAdapter<ProfileQuests>(
        { out, value -> debug("serializing"); value.serialize(out) },
        { throw UnsupportedOperationException("Can't deserialize Quests. Use ProfileQuests.quests") }
    )
    val MSGHolderTypeAdapter = typeAdapter<MSGHolder>(
        { out, value -> out.value(value.id) },
        { LangManager.getMessage(it.nextString()) }
    )

    val gson: Gson = GsonBuilder()
        .register(BigIntegerTypeAdapter)
        .register(UUIDTypeAdapter)
        .register(LanguageCodeTypeAdapter)
        .register(EquipableInventoryTypeAdapter)
        .register(ProfileLocationsTypeAdapter)
        .register(QuestsTypeAdapter)
        .register(MSGHolderTypeAdapter)
        .create()
}