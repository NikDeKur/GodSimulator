@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.profile

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.ndk.global.interfaces.Snowflake
import org.ndk.global.placeholders.Placeholder
import org.ndk.godsimulator.GodSimulator.Companion.godsManager
import org.ndk.godsimulator.GodSimulator.Companion.languagesManager
import org.ndk.godsimulator.GodSimulator.Companion.locationsManager
import org.ndk.godsimulator.buying.Buyable
import org.ndk.godsimulator.database.Database.Companion.accessor
import org.ndk.godsimulator.database.Database.Companion.accessorAsync
import org.ndk.godsimulator.database.PlayerAccessor
import org.ndk.godsimulator.event.profile.ProfileLevelChangeEvent
import org.ndk.godsimulator.event.profile.ProfileStaminaChangeEvent
import org.ndk.godsimulator.god.ForceGodSelectGUI
import org.ndk.godsimulator.god.God
import org.ndk.godsimulator.god.NotSelectedGod
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.godsimulator.location.SimulatorLocation
import org.ndk.godsimulator.rpg.profile.RPGProfile
import org.ndk.klib.*
import org.ndk.minecraft.CooldownHolder
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.language.MSGHolder
import org.ndk.minecraft.plugin.ServerPlugin.Companion.callEvent
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap

class PlayerProfile(
    val globalAccessor: PlayerAccessor,
    override val nameMSG: MSGHolder,
    override val id: UUID,
    val accessor: MutableMap<String, Any>
) : Placeholder, MSGNameHolder, CooldownHolder, Snowflake<UUID> {

    init {
        accessor["iconName"] = nameMSG.id
    }

    override val defaultPhName: String = "profile"

    val playerId: UUID = globalAccessor.playerId
    val player: OfflinePlayer = globalAccessor.player
    val onlinePlayer: Player?
        get() = playerId.onlinePlayerOrNull

    val reference = Reference(playerId, id)



    val scopes = ProfileScopes(this)
    val wallet = ProfileWallet(this)
    val skills = ProfileSkills(this)
    val rpg = RPGProfile(this)




    override val cooldowns: MutableMap<String, Double> by accessor.mutableMapBoundVar("cooldowns")
    override var passCooldown by accessor.booleanBoundVar("passCooldown", default = false)

    override val placeholderMap: MutableMap<String, Any>
        get() = super<MSGNameHolder>.placeholderMap.also {
            val createdLDT = LocalDateTime.ofEpochSecond(created / 1000, 0, ZoneOffset.UTC)
            it["created"] = createdLDT.format(PROFILE_CREATED_FORMAT)
            it["createdFull"] = createdLDT.format(PROFILE_CREATED_FORMAT_FULL)
            it["bagFill"] = bagFill.toBeautifulString()
            it["bagSize"] = bag.type.size.toBeautifulString()
            it["level"] = level.toBeautifulString()
            it["xp"] = xp.toBeautifulString()
            it["xpForNextLevel"] = xpForNextLevel.toBeautifulString()
            it["xpToNextLevel"] = xpToNextLevel.toBeautifulString()
        }

    /**
     * The time when the player profile was created.
     *
     * Time is stored in ms from epoch.
     */
    val created by scopes::created

    /**
     * The language code of the player profile.
     *
     * Example format: en_us, ru_ru
     *
     * If the language is not found, it will return the .
     */
    var languageCode by scopes::language


    /**
     * The language of the player profile.
     *
     * If the language is not found, it will return the default language.
     */
    var language
        get() = languagesManager[languageCode] ?: languagesManager.defaultLang
        set(value) { languageCode = value.code }


    /**
     * The stamina profile of the player.
     *
     * Stamina is used to do double jump.
     */
    val stamina by scopes::stamina

    /**
     * The max stamina profile of the player.
     */
    val maxStamina by scopes::maxStamina

    /**
     * Fill the stamina with the player.
     *
     * If the new stamina is greater than the max stamina, it will be set to the max stamina.
     */
    fun fillStamina(amount: Int) {
        var new = stamina + amount
        if (new > maxStamina) new = maxStamina

        val event = ProfileStaminaChangeEvent(this, stamina, new)
        callEvent(event)
        if (event.isCancelled) return
        val finalNew = event.newStamina

        scopes.stamina = finalNew
    }

    /**
     * Take the stamina from the player.
     *
     * @param amount the amount of stamina to take
     * @return has the stamina been taken?
     * @see fillStamina
     */
    fun takeStamina(amount: Int): Boolean {
        require(amount >= 0) { "Amount must be greater than or equal to 0"}
        if (stamina < amount) return false

        val new = stamina - amount

        val event = ProfileStaminaChangeEvent(this, stamina, new)
        callEvent(event)
        if (event.isCancelled) return false
        val finalNew = event.newStamina

        scopes.stamina = finalNew
        return true
    }


    /**
     * Get the god of the player.
     *
     * If the player has no god, it will return [NotSelectedGod].
     */
    val god: God
        get() = scopes.god?.let { godsManager.getGod(it) } ?: NotSelectedGod

    fun setGod(god: God, silent: Boolean = false) {
        this.scopes.god = god.id
        updateUnlockedSkills(true)
        if (!silent) {
            val player = playerId.onlinePlayerOrNull
            if (player != null) {
                val placeholder = god.getFinalPlaceholder(player)
                player.sendLangMsg(MSG.GOD_SELECT_SUCCESS, placeholder)
            }
        }
    }

    val forceSelectGod by scopes::forceSelectGod

    fun forceSelectGod() {
        scopes.forceSelectGod = true
        val player = playerId.onlinePlayerOrNull
        if (player != null) {
            ForceGodSelectGUI(player, this).open()
        }
    }

    fun stopForceSelectGod(god: God, silent: Boolean = false) {
        scopes.forceSelectGod = false
        setGod(god, silent)
    }


    val level by scopes::level
    val xp by scopes::xp

    val xpForNextLevel: BigInteger
        get() = LevelManager.getXpForLevel(level + 1)
    val xpToNextLevel: BigInteger
        get() = xpForNextLevel - xp

    /**
     * Give the specified amount of experience to the player.
     *
     * Automatically scales the experience by RPGBuffs.
     *
     * If the experience is reached the next level, it will level up the player.
     *
     * @param amount the amount of experience to give
     * @return the final amount of experience given
     * @see LevelManager.giveExperience
     */
    fun giveExperience(amount: BigInteger): BigInteger {
        val finalAmount = rpg.scaleExp(amount)
        LevelManager.giveExperience(this, finalAmount)
        updateMinecraftXpBar()
        return finalAmount
    }

    /**
     * Level up the player specified number of times.
     *
     * If the amount is not specified, it will level up the player once.
     *
     * @param amount the number of times to level up. Default is 1.
     * @see LevelManager.giveExperience
     */
    fun levelUp(amount: Int = 1) {
        require(amount > 0) { "Amount must be greater than 0" }
        val newRaw = level + amount

        val event = ProfileLevelChangeEvent(this, level, newRaw)
        callEvent(event)
        if (event.isCancelled) return
        val new = event.newLevel

        scopes.level = new
        onlinePlayer?.sendLangMsg(MSG.LEVEL_UP, "level" to new)
        updateMinecraftXpBar()
        updateUnlockedSkills()
    }


    fun sellBagFill(): BigInteger {
        val final = rpg.scaleCoins(bagFill)
        clearBag()
        wallet.giveCoins(final)
        return final
    }


    fun giveCoins(amount: BigInteger) {
        val final = rpg.scaleCoins(amount)
        wallet.giveCoins(final)
    }

    fun giveSouls(amount: BigInteger) {
        val final = rpg.scaleSouls(amount)
        wallet.giveSouls(final)
    }





    /**
     * The number of blocks in the bag.
     *
     * To give or take blocks, use [fillBag] instead.
     */
    val bagFill by scopes::bagFill


    /**
     *
     * Fill the bag with the given amount.
     *
     * If the bag is set to auto sell, the amount will be added to the balance.
     *
     * Automatically scales the number of blocks by RPGBuffs.
     *
     * @param amount the number of blocks to fill the bag with
     * @param silent should the player be notified if the bag is full?
     * @return has a bag been fully filled
     */
    fun fillBag(amount: BigInteger, silent: Boolean = false): Boolean {
        val type = bag.type

        if (type.isAutoSell) {
            wallet.giveCoins(amount)
            return true
        }

        val bagSize = type.size
        if (!bag.type.isInfinity && (bagFill + amount) >= bagSize) {
            scopes.bagFill = bagSize
            if (!silent)
                playerId.onlinePlayerOrNull?.sendTitleLangMsg(MSG.BAG_FULL)
            return true
        } else {
            scopes.bagFill += amount
        }
        return false
    }

    fun clearBag() {
        scopes.bagFill = BigInteger.ZERO
    }

    val items by scopes::items
    val itemsEquipLimit by scopes::itemsEquipLimit

    val petsLimit by scopes::petsLimit
    val petsEquipLimit by scopes::petsEquipLimit
    val pets by scopes::pets

    val auras by scopes::auras


    val bags by scopes::bags
    val bag by bags::bag





    val rebirth by scopes::rebirth

    fun canRebirth(): Boolean {
        return level >= ((rebirth + 1) * 100)
    }

    fun rebirth(amount: Int, silent: Boolean = false): Boolean {
        val player = playerId.onlinePlayerOrNull

        val placeholder = mapOf(
            "profile" to this.toSingletonSet(),
            "rebirth" to setOf(
                Placeholder.ofSingle("given", amount),
                Placeholder.ofSingle("total", rebirth),
                Placeholder.ofSingle("requiredLevel", ((rebirth + 1) * 100)),
                Placeholder.ofSingle("currentLevel", level)
            )
        )

        if (!canRebirth()) {
            if (player != null && !silent) {
                player.sendLangMsg(MSG.REBIRTH_REQUIRE_LEVEL, placeholder)
            }
            return false
        }

        scopes.rebirth += amount

        // Reset Everything, except god
        scopes.level = 1
        scopes.xp = BigInteger.ZERO
        clearBag()

        wallet.resetCoins()
        // Do not reset souls

        // Clear bags, It will set the default bag
        bags.clear()

        // Do not clear auras and pets

        updateMinecraftXpBar()
        updateUnlockedSkills()


        if (player != null) {
            if (!silent) {
                val msg = if (amount > 1)
                    MSG.REBIRTH_SEVERAL_TIMES_SUCCESS
                else
                    MSG.REBIRTH_ONCE_SUCCESS

                player.sendLangMsg(msg, placeholder)
            }
        }

        return true
    }






    var passLocations by accessor.booleanBoundVar("passLocations", default = false)
    var passSkillCast by accessor.booleanBoundVar("passSlotLock", default = false)
    var passAdventure by accessor.booleanBoundVar("passAdventure", default = false)

    val unlockedLocations by scopes::unlockedLocations

    /**
     * Unlock the location for the player.
     *
     * If the location is already unlocked, it will do nothing.
     *
     * @param location the location to unlock
     * @param silent should the player be notified about the unlocking?
     */
    fun unlockLocation(location: SimulatorLocation, silent: Boolean = false) {
        if (hasUnlockedLocation(location)) return
        unlockedLocations.add(location)
        val player = onlinePlayer
        if (player != null && !silent) {
            player.sendLangMsg(MSG.LOCATION_UNLOCK_SUCCESS, location.getFinalPlaceholder(player))
        }
    }

    fun hasUnlockedLocation(location: SimulatorLocation): Boolean {
        return locationsManager.defaultLocations.contains(location.id) ||
                unlockedLocations.contains(location) ||
                passLocations
    }


    inline fun buy(buyable: Buyable) {
        buyable.buy(this)
    }


    fun onCreated() {
        scopes.forceSelectGod = true
    }

    fun updateMinecraftXpBar() {
        val player = playerId.onlinePlayerOrNull ?: return

        // If some xp changes occurs and required xp for the next level is less than current xp
        // Update the current level
        if (xp >= xpForNextLevel) {
            scopes.xp--
            LevelManager.giveExperience(this, BigInteger.ZERO)
        }

        player.level = level
        player.setExp(xp, xpForNextLevel)
    }


    fun updateUnlockedSkills(bind: Boolean = false) {
        god.updateUnlockedSkills(skills, bind)
    }


    fun onSelected(): Boolean {
        rpg.applyOnPlayer()

        updateMinecraftXpBar()
        updateUnlockedSkills()


        return true
    }


    fun onUnSelected(): Boolean {
        val player = playerId.onlinePlayerOrNull ?: return false

        rpg.unApplyOnPlayer()

        player.level = 0
        player.exp = 0f

        return true
    }


    /**
     * Creates a new profile object with current data copy to another accessor
     *
     * Doesn't register profile in the accessor, only create new [PlayerProfile] object with another accessor and random UUID.
     *
     * @param toAccessor accessor to copy into
     * @return profile object
     */
    fun clone(toAccessor: PlayerAccessor): PlayerProfile {
        return PlayerProfile(
            toAccessor,
            nameMSG,
            UUID.randomUUID(),
            ConcurrentHashMap(this.scopes.accessor)
        )
    }



    fun delete() {
        globalAccessor.deleteProfile(id)
    }


    /**
     * Get the icon of the profile.
     *
     * Icon is format from the god icon with additional information about the profile.
     *
     * Tags:
     * - isProfile: true
     * - playerId: player UUID (str)
     * - profileId: profile UUID (str)
     * - profileSelected: true/false
     *
     * @param player the player to get the icon for
     * @return the icon of the profile
     */
    override fun getIcon(player: Player): ItemStack {
        val placeholder = mapOf(
            this.getPairPlaceholder(player),
            god.getPairPlaceholder(player)
        )
        val display = player.getLangMsg(MSG.PROFILE_ICON_DISPLAY, placeholder).text
        val lore = player.getLangMsg(MSG.PROFILE_ICON_LORE, placeholder).listText

        val profilePlayerId = playerId

        return god.getIcon(player)
            .setDisplayName(display)
            .setLore(lore)
            .setTouchable(false)
            .setTag("isProfile", true)
            .setTag("playerId", playerId.toString())
            .setTag("profileId", id.toString())
            .also {
                val loree = it.lore
                loree.add("")
                if (profilePlayerId == player.uniqueId && id == globalAccessor.currentProfileId) {
                    it.setTag("profileSelected", true)
                    loree.add(player.getLangMsg(MSG.PROFILE_ICON_SELECTED, placeholder).text)
                } else {
                    it.setTag("profileSelected", false)
                    loree.add(player.getLangMsg(MSG.PROFILE_ICON_NOT_SELECTED, placeholder).text)
                }
                it.setLore(loree)
            }
    }



    data class Reference(
        val playerId: UUID,
        override val id: UUID,
    ) : Snowflake<UUID> {

        val profile: CompletableFuture<PlayerProfile>
            get() = playerId.onlinePlayerOrNull?.getProfileAsync(id)
                ?: CompletableFuture.completedFuture(null)
    }


    companion object {
        fun Player.getProfile(profileId: UUID) = accessor.getProfile(profileId)

        /**
         * Static access method to
         * ```
         * accessorAsync.thenApply { it.getProfile(profileId) }
         * ```
         * @see accessorAsync
         * @see PlayerAccessor.getProfile
         */
        fun OfflinePlayer.getProfileAsync(profileId: UUID): CompletableFuture<PlayerProfile> {
            return accessorAsync.thenApply { it.getProfile(profileId) }
        }

        /**
         * Static access method to
         * ```
         * accessor.getProfileOrNull(profileId)
         * ```
         * @see PlayerAccessor.getProfileOrNull
         * @see accessor
         */
        fun Player.getProfileOrNull(profileId: UUID): PlayerProfile? {
            return accessor.getProfileOrNull(profileId)
        }

        fun randomName(accessor: PlayerAccessor): MSGHolder {
            val used = accessor.profiles.values.map { it.nameMSG }
            val notUsed = PROFILES_NAMES.filter { it !in used }
            if (notUsed.isEmpty()) return PROFILES_NAMES.random()
            return notUsed.random()
        }



        val Player.profile: PlayerProfile
            get() = accessor.selectedProfile
        val OfflinePlayer.profileAsync: CompletableFuture<PlayerProfile>
            get() = accessorAsync.thenApply { it.profile }



        val PROFILES_NAMES = listOf(
            MSG.PROFILE_RANDOM_NAME_1,
            MSG.PROFILE_RANDOM_NAME_2,
            MSG.PROFILE_RANDOM_NAME_3,
            MSG.PROFILE_RANDOM_NAME_4,
            MSG.PROFILE_RANDOM_NAME_5,
            MSG.PROFILE_RANDOM_NAME_6,
            MSG.PROFILE_RANDOM_NAME_7,
            MSG.PROFILE_RANDOM_NAME_8,
            MSG.PROFILE_RANDOM_NAME_9,
            MSG.PROFILE_RANDOM_NAME_10,
        )

        val PROFILE_CREATED_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val PROFILE_CREATED_FORMAT_FULL: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    }
}


