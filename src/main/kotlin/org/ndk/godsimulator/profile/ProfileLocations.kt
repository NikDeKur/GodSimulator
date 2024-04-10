package org.ndk.godsimulator.profile

import com.google.gson.reflect.TypeToken
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.database.Database
import org.ndk.godsimulator.location.SimulatorLocation
import org.ndk.minecraft.Utils.debug
import java.lang.reflect.Type

class ProfileLocations(val profile: PlayerProfile) : HashSet<SimulatorLocation>() {


    /**
     * Serialize the locations to a string collection
     *
     * Example: ["location1", "location2", "location3"]
     *
     * @return The serialized string
     */
    fun serialize(): Collection<String> {
        return map(SimulatorLocation::id)
    }

    companion object {

        val TYPE: Type = object : TypeToken<Array<String>>() {}.type

        fun fromSerialized(
            profile: PlayerProfile,
            serialized: String
        ): ProfileLocations {
            val list = Database.GSON.fromJson<Array<String>>(serialized, TYPE)

            val locations = ProfileLocations(profile)

            for (id in list) {
                val location = GodSimulator.locationsManager.getLocation(id)
                if (location != null) {
                    locations.add(location)
                }
            }

            return locations
        }
    }
}