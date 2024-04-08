@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.profile

import com.google.gson.reflect.TypeToken
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.database.Database
import org.ndk.godsimulator.location.SimulatorLocation
import java.lang.reflect.Type

class ProfileLocations(val profile: PlayerProfile) : HashSet<SimulatorLocation>() {



    fun serialize(): String {
        return map { it.id }.toString()
    }

    companion object {
        val GSON_TYPE: Type = object : TypeToken<HashSet<String>>() {}.type

        fun fromSerialized(
            profile: PlayerProfile,
            serialized: String
        ): ProfileLocations {
            val set = Database.GSON.fromJson<HashSet<String>>(serialized, GSON_TYPE)

            val locations = ProfileLocations(profile)

            for (id in set) {
                val location = GodSimulator.locationsManager.getLocation(id)
                if (location != null) {
                    locations.add(location)
                }
            }

            return locations
        }
    }
}