package org.ndk.godsimulator.profile

import org.ndk.godsimulator.GodSimulator.Companion.equipableManager
import org.ndk.godsimulator.GodSimulator.Companion.languagesManager
import org.ndk.godsimulator.equipable.impl.AurasInventory
import org.ndk.godsimulator.equipable.impl.BagsInventory
import org.ndk.godsimulator.equipable.impl.PetsInventory
import org.ndk.godsimulator.equipable.inventory.EquipableInventory
import org.ndk.godsimulator.equipable.type.EquipableType
import org.ndk.godsimulator.equipable.type.EquipableTypesManager
import org.ndk.godsimulator.utils.ClassDataHolderField
import org.ndk.godsimulator.utils.InventoryHolderField
import org.ndk.godsimulator.utils.NotNullClassDataHolderField
import org.ndk.klib.*
import org.ndk.minecraft.language.Language

/**
 * Class is used to store the player profile scopes.
 *
 * Scopes in this class is used by [PlayerProfile] class with safe mechanisms.
 * Developers should use [PlayerProfile] to access the scopes, but only use this class in situations where you need to access the scopes directly.
 *
 * Using this class directly can cause unexpected behaviour, be careful.
 *
 * @param profile The player profile
 */
class ProfileScopes(
    val profile: PlayerProfile
) {

    val accessor = profile.accessor


    /**
     * The time when the player profile was created.
     *
     * Time is stored in ms from epoch.
     *
     * If no value was provided for some reason, it will be set to the current time.
     */
    var created by accessor.longBoundVar("created", default = System.currentTimeMillis())

    /**
     * The language code of the player profile or the default language code if not set.
     */
    var language by notNullClassDataHolder(
        "language",
        "en"
    ) { when (it) {
            is Language.Code -> it
            is String -> Language.Code.fromCode(it) ?: languagesManager.defaultLangCode
            else -> languagesManager.defaultLangCode
        }
    }

    /**
     * The stamina profile of the player.
     *
     * Stamina is used to do double jump.
     */
    var stamina by accessor.intBoundVar("stamina", default = 100)

    /**
     * The max stamina profile of the player.
     *
     * The Setting of this value will not affect the current stamina of the player.
     *
     * @see [stamina]
     */
    var maxStamina by accessor.intBoundVar("maxStamina", default = 100)


    /**
     * The godId of the player profile.
     *
     * If the player has not selected a god, it will return null.
     *
     * If you want to get the god of the player or not selected god, use [PlayerProfile.god] instead.
     */
    var god by accessor.stringBoundVar("god")


    /**
     * Parameter to force the player to select a god.
     *
     * If true, the player will be forced to select a god when they join the server.
     *
     * It will include:
     * - Always opened selection GUI
     * - Moving disabled
     *
     * To control, use [PlayerProfile.forceSelectGod] and [PlayerProfile.stopForceSelectGod]
     */
    var forceSelectGod by accessor.booleanBoundVar("forceSelectGood")

    /**
     * The level of the player.
     */
    var level by accessor.intBoundVar("level", default = 1)

    /**
     * The experience of the player.
     */
    var xp by accessor.bigIntBoundVar("xp")

    /**
     * The rebirth of the player.
     */
    var rebirth by accessor.intBoundVar("rebirth", default = 0)

    /**
     * The map currency id to the amount of the player.
     */
    val wallet: MutableMap<String, Any> by accessor.mutableMapBoundVar("wallet")

    /**
     * The map of the skill bindings of the player.
     */
    val skillBindings: MutableMap<String, Any> by accessor.mutableMapBoundVar("skillBindings")

    val unlockedSkills: MutableSet<String> by accessor.mutablePrimitiveSetBoundVar("unlockedSkills")

    /**
     * The bag fill of the player.
     */
    var bagFill by accessor.bigIntBoundVar("bagFill")



    /**
     * The limit of the pets, that the player can have. Include equipped and unequipped pets.
     */
    val petsLimit by accessor.intBoundVar("petsLimit", default = 200)

    /**
     * The limit of the pets, that the player can have equipped.
     */
    val petsEquipLimit by accessor.intBoundVar("petsEquipLimit", default = 3)
    val pets by inventoryDataHolder(
        "pets",
        PetsInventory::class.java,
        equipableManager.pets
    )



    val aurasLimit by accessor.intBoundVar("petsLimit", default = 200)
    val auras by inventoryDataHolder(
        "auras",
        AurasInventory::class.java,
        equipableManager.auras
    )




    val bags by inventoryDataHolder(
        "bags",
        BagsInventory::class.java,
        equipableManager.bags
    )


    val unlockedLocations by notNullClassDataHolder(
        "unlockedLocations",
        "[]",
    ) {
        when (it) {
            is ProfileLocations -> it
            else -> ProfileLocations.fromSerialized(profile, it.toString())
        }
    }

    //----------------------------------------
    // Utils Start
    //----------------------------------------
    private fun <T : Any> classDataHolder(
        path: String,
        stringDefault: Any,
        deserialize: (Any) -> T
    ): ClassDataHolderField<T> {
        return ClassDataHolderField(profile, path, stringDefault, deserialize)
    }

    private fun <T : Any> notNullClassDataHolder(
        path: String,
        stringDefault: Any,
        deserialize: (Any) -> T
    ): NotNullClassDataHolderField<T> {
        return NotNullClassDataHolderField(profile, path, stringDefault, deserialize)
    }

    private fun <T : EquipableType<T>, CLZ : EquipableInventory<T>> inventoryDataHolder(
        path: String,
        inventoryClazz: Class<CLZ>,
        manager: EquipableTypesManager<T>
    ): InventoryHolderField<T, CLZ> {
        return InventoryHolderField(profile, path, inventoryClazz, manager)
    }

    //----------------------------------------
    // Utils End
    //----------------------------------------
}