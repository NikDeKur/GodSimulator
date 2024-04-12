@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.database

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.ndk.database.session.SessionImpl
import org.ndk.godsimulator.GodSimulator.Companion.languagesManager
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.klib.mutableMapBoundVar
import org.ndk.klib.uuid
import org.ndk.klib.uuidBoundVar
import org.ndk.minecraft.CooldownHolder
import org.ndk.minecraft.language.MSGHolder
import org.ndk.minecraft.plugin.ServerPlugin.Companion.bLogger
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Suppress("LeakingThis")
open class PlayerAccessor(
    service: PlayersDataService,
    val player: OfflinePlayer
) : SessionImpl<OfflinePlayer, UUID, PlayerAccessor>(service, player), CooldownHolder {

    override val passCooldown: Boolean
        get() = player.isOp

    val playerId: UUID = player.uniqueId
    val name: String
        get() = player.name

    val onlinePlayer: Player
        get() = if (player.isOnline) (player as Player) else throw IllegalStateException("Player is not online")

    /**
     * The profiles of the player.
     *
     * Map is concurrent to avoid concurrent modification exceptions and don't save the order of profiles.
     */
    val profiles = ConcurrentHashMap<UUID, PlayerProfile>()

    /**
     * The currently selected profile or null.
     *
     * Private to avoid setting it to null without calling [PlayerProfile.onUnSelected]
     */
    private var _selectedProfile: PlayerProfile? = null

    /**
     * Whether any profile is selected.
     *
     * Usually this always returns true, but it's possible to have no profiles with [clearProfiles] or some other way.
     */
    val isAnyProfileSelected: Boolean
        get() = _selectedProfile != null

    /**
     * The currently selected profile.
     *
     * Throws an exception if no profile is selected (Usually not happen).
     *
     * @see isAnyProfileSelected
     */
    var selectedProfile: PlayerProfile
        get() = _selectedProfile ?: throw IllegalStateException("No profile selected")
        private set(value) {
            _selectedProfile = value
        }

    /**
     * The currently selected profile.
     *
     * Alias for [selectedProfile], delegates to it, but unchangeable any way.
     *
     * Throw an exception if no profile is selected (Usually not happen).
     *
     * @see selectedProfile
     * @see isAnyProfileSelected
     */
    val profile by ::selectedProfile


    init {
        // Load profiles
        afterLoadHooks.add {
            profilesMap.forEach { (profileIdStr, profileMap) ->
                try {
                    // Get icon from the profile map
                    val nameMSG = (profileMap["iconName"] as? String)?.let(languagesManager::getMessage)
                    var id = profileIdStr.uuid
                    if (id == null) {
                        id = UUID.randomUUID()
                        bLogger.warning("Invalid profile id: $profileIdStr. Generating new id ($id).")
                    }

                    newProfile(false, id!!, nameMSG)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            // Select the current profile
            val currentProfileId = currentProfileId ?: UUID.randomUUID()
            val current = profiles[currentProfileId] ?: newProfile(true, currentProfileId)
            selectProfile(current)
        }

        // Selected Profile unselecting will be executed in [Database] while ending the session
        // If we add it as beforeSaveTask it will unselect profile every save time.

        // Clean useless cooldowns
        val cleaner = getCooldownCleaner(this)
        afterLoadHooks.add(cleaner)
        beforeSaveHooks.add(cleaner)
    }

    override val cooldowns: MutableMap<String, Double> by mutableMapBoundVar("cooldowns")

    /**
     * Gets the uuid of the currently selected profile.
     */
    var currentProfileId by uuidBoundVar("currentProfileId")

    /**
     * Selects a profile by its object reference.
     * @param profile The profile object to select.
     */
    fun selectProfile(profile: PlayerProfile) {
        if (_selectedProfile != null) {
            selectedProfile.onUnSelected()
        }
        currentProfileId = profile.id
        selectedProfile = profile
        profile.onSelected()
    }

    /**
     * Deletes a profile by its profileId (UUID).
     *
     * If the profile to delete is the currently selected profile, then the next profile will be selected.
     * If the profile to delete is the last profile, then a new profile will be created and selected.
     *
     * @param profileId The index of the profile to delete.
     * @throws IllegalArgumentException If the profile is not found.
     */
    fun deleteProfile(profileId: UUID) {
        if (!profiles.containsKey(profileId))
            throw IllegalArgumentException("Profile with uuid $profileId not found in PlayerAccessor of $playerId")

        if (selectedProfile.id == profileId) {
            selectedProfile.onUnSelected()
            _selectedProfile = null
        }

        profilesMap.remove(profileId.toString())
        profiles.remove(profileId)

        // If deleted profile was the latest profile, then create and select a new one
        if (profiles.isEmpty()) {
            selectProfile(newProfile())

        // If deleted profile was the selected profile, then select the next profile
        } else if (_selectedProfile == null) {
            selectProfile(profiles.values.first())
        }
    }

    /**
     * Clears all profiles and profile's data from the accessor.
     *
     * Unselect the selected profile if it exists
     *
     * @param createEmpty If true, a new empty profile will be created. IF SET TO FALSE, THE ATTEMPT TO GET [selectedProfile] WILL END UP WITH AN EXCEPTION
     */
    fun clearProfiles(createEmpty: Boolean = true) {
        if (_selectedProfile != null) {
            selectedProfile.onUnSelected()
            _selectedProfile = null
        }
        profiles.clear()
        profilesMap.clear()

        if (createEmpty) {
            selectProfile(newProfile())
        }
    }


    /**
     * Gets the map of profiles.
     * @return The mutable map containing profiles.
     */
    val profilesMap: MutableMap<String, MutableMap<String, Any>> by mutableMapBoundVar("profiles", mapGen = { ConcurrentHashMap() })

    /**
     * Creates a new profile with the given uuid and iconName.
     *
     * If data of this profile already exists in [profilesMap] it will be used.
     *
     * Register profile and call [PlayerProfile.onCreated] method.
     *
     * @param newlyCreated If true, [PlayerProfile.onCreated] will be called.
     * @param id UUID of profile
     * @param iconName The MSGHolder representing random profile name. If not provided, a random name will be chosen.
     * @return The newly created profile.
     * @throws IllegalArgumentException If the profile with the given uuid already exists.
     */
    @Synchronized
    fun newProfile(newlyCreated: Boolean, id: UUID, iconName: MSGHolder? = null): PlayerProfile {
        if (profiles.containsKey(id)) {
            throw IllegalArgumentException("Profile with uuid $id already exists in PlayerAccessor of $playerId")
        }
        val finalNameMSG = iconName ?: PlayerProfile.randomName(this)
        val profileMap = profilesMap.computeIfAbsent(id.toString()) { ConcurrentHashMap() }
        val profile = PlayerProfile(this, finalNameMSG, id, profileMap)
        profiles[id] = profile
        if (newlyCreated)
            onNewlyCreatedProfile(profile)
        return profile
    }

    fun onNewlyCreatedProfile(profile: PlayerProfile) {
        profile.scopes.created = System.currentTimeMillis()
        profile.onCreated()
    }

    /**
     * Creates a new profile with the random uuid.
     * @return The created profile.
     * @see newProfile
     */
    fun newProfile(newlyCreated: Boolean = true) = newProfile(newlyCreated, UUID.randomUUID())

    /**
     * Copies a profile to this accessor.
     *
     * Firstly, it clones the profile, then it copies the profile data to the accessor.
     *
     * Profile UUID is generated by [PlayerProfile.clone] and used here.
     *
     * If the profilesMap contains the new generated UUID, old data will be cleared and replaced with the new one.
     *
     * @param profile The profile to copy
     * @return The copied profile.
     */
    fun copyProfile(profile: PlayerProfile): PlayerProfile {
        val copied = profile.clone(this)
        val id = copied.id
        val profileMap = profilesMap.computeIfAbsent(id.toString()) { ConcurrentHashMap() }
        profileMap.clear()
        profileMap.putAll(copied.scopes.accessor)
        profiles[id] = copied
        return copied
    }


    /**
     * Gets the profile with the given profileId, or creates a new one if it doesn't exist.
     *
     * @param profileId The uuid of the profile to get.
     * @return The profile with the specified index, or a new one if not found.
     * @see getProfileOrNull
     * @see newProfile
     */
    fun getProfile(profileId: UUID): PlayerProfile {
        return getProfileOrNull(profileId) ?: newProfile(true, profileId)
    }

    /**
     * Gets the profile with the given profileId, or null if it doesn't exist.
     *
     * Throw an exception if accessor is not loaded
     *
     * @param profileId The uuid of the profile to get.
     * @return The profile with the specified index, or null if not found.
     * @see getProfile
     */
    fun getProfileOrNull(profileId: UUID): PlayerProfile? {
        ensureLoaded()
        return profiles[profileId]
    }

    /**
     * Clears all data from the accessor.
     *
     * Clear profiles [clearProfiles]
     *
     * Clear the accessor data
     *
     * @param createEmpty If true, a new empty profile will be created. IF SET TO FALSE, THE ATTEMPT TO GET [selectedProfile] WILL END UP WITH AN EXCEPTION
     * @see clearProfiles
     */
    fun clearAccessor(createEmpty: Boolean) {
        clearProfiles(createEmpty)
        super.clear()
    }

    companion object {
        fun getCooldownCleaner(accessor: PlayerAccessor): () -> Unit {
            return {
                checkCooldowns(accessor.cooldowns)
                accessor.profiles.values.forEach { profile ->
                    checkCooldowns(profile.cooldowns)
                }
            }
        }

        inline fun checkCooldowns(map: MutableMap<String, Double>) {
            val now = System.currentTimeMillis()
            map.entries.removeIf { it.value < now }
        }
    }
}
