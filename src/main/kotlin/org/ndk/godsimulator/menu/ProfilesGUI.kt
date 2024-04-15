package org.ndk.godsimulator.menu

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.ndk.godsimulator.database.Database.accessor
import org.ndk.godsimulator.database.Database.accessorAsync
import org.ndk.godsimulator.database.PlayerAccessor
import org.ndk.godsimulator.extension.setTexture
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.language.Quick
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.utils.ConfirmationGUI
import org.ndk.klib.uuid
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.gui.GUI
import org.ndk.minecraft.gui.GUIFlag
import org.ndk.minecraft.item.ItemPattern
import org.ndk.minecraft.item.Patterns
import java.time.Duration
import java.util.*

class ProfilesGUI(player: Player) : GUI(player, 27) {

    override fun getTitle(): String {
        return player.getLangMsg(MSG.PROFILES_GUI_TITLE).text
    }

    override val flags: Set<GUIFlag> = setOf(GUIFlag.CANNOT_TAKE, GUIFlag.CANNOT_PUT)

    override fun beforeOpen() {
        inventory.setRow(1, Patterns.EMPTY_SLOT)
        inventory.setRow(3, Patterns.EMPTY_SLOT)
        inventory.setRange(9..10, Patterns.EMPTY_SLOT)
        inventory.setRange(16..17, Patterns.EMPTY_SLOT)

        val accessor = player.accessor
        val profiles = ArrayList(accessor.profiles.values)
        profiles.sortBy { it.created }
        profiles.forEach {
            inventory.addItem(it.getIcon(player))
        }
        val profileCount = profiles.size
        if (profileCount < 5) {
            val slots = LinkedList<ItemPattern>()
            slots.add(CREATE_PROFILE_MVP)
            if (profileCount < 4) {
                slots.add(CREATE_PROFILE_PREMIUM)
            }
            if (profileCount < 3) {
                slots.add(CREATE_PROFILE_GOLD)
            }
            if (profileCount < 2) {
                slots.add(CREATE_PROFILE_FREE)
            }
            slots
                .descendingIterator()
                .forEach { pattern ->
                    pattern.onBuild { it, _ ->
                        if (it.getBooleanTag("requireDonate") == true) {
                            val require = it.getStringTag("requiredDonate")!!
                            val hasDonate = accessor["is$require"] as? Boolean ?: false
                            if (!hasDonate) {
                                it.setLore(
                                    player.getLangMsg(
                                    MSG.PROFILE_CREATE_ICON_REQUIRE_DONATE_LORE,
                                    "donate" to require)
                                    .listText
                                )
                                it.setTexture("lock")
                                it.setTag("canCreate", false)
                                return@onBuild
                            }
                        }
                        it.setLore(player.getLangMsg(MSG.PROFILE_CREATE_ICON_LORE).listText)
                        it.setTexture("available")
                        it.setTag("canCreate", true)
                    }
                    inventory.addItem(pattern.build(player))
                }
        }
    }

    override fun onClick(event: InventoryClickEvent) {
        val item = event.currentItem ?: return
        if (item.getBooleanTag("isProfileAdd") == true) {
            onCreate(item)
            update()
            return
        }
        if (item.getBooleanTag("isProfile") != true) return
        val playerId = item.getStringTag("playerId")?.uuid
        val profileId = item.getStringTag("profileId")?.uuid
        // If no tag provided or tag is not uuid
        if (playerId == null || profileId == null) {
            Quick.internalError(player, "Profile icon has incorrect signature. Report to admin. PlayerId: $playerId, ProfileId: $profileId")
            closeAndFinish()
            return
        }
        val player = playerId.player
        player.accessorAsync.thenApply {
            if (event.isLeftClick) onSelect(it, item, playerId, profileId)
            else if (event.isRightClick) onDelete(it, playerId, profileId)
        }
        update()
    }


    fun onSelect(accessor: PlayerAccessor, item: ItemStack, playerId: UUID, profileId: UUID) {
        if (item.getBooleanTag("profileSelected") == true) return
        val executorAccessor = player.accessor
        if (Quick.checkCooldown(player, executorAccessor, "profileSelect", MSG.PROFILE_SELECT_COOLDOWN))
            return
        val profileRaw = accessor.getProfileOrNull(profileId)
        if (profileRaw == null) {
            Quick.internalError(player, "Profile not found. Report to admin. PlayerId: $playerId, ProfileId: $profileId")
            return
        }

        val profile = if (player.uniqueId != playerId) {
            val copiedProfile = executorAccessor.copyProfile(profileRaw)
            executorAccessor.selectProfile(copiedProfile)
            copiedProfile
        } else {
            executorAccessor.selectProfile(profileRaw)
            profileRaw
        }

        executorAccessor.setCooldown("profileSelect", Duration.ofMinutes(5))

        player.sendLangMsg(MSG.PROFILE_SELECTED, profile.getFinalPlaceholder(player))
    }

    fun onCreate(item: ItemStack) {
        if (item.getBooleanTag("canCreate") != true) return
        player.accessorAsync.thenAccept {
            val profile = it.newProfile()
            it.selectProfile(profile)
            profile.forceSelectGod()
            player.sendLangMsg(MSG.PROFILE_CREATED, profile.getFinalPlaceholder(player))
        }
    }

    fun onDelete(accessor: PlayerAccessor, playerId: UUID, profileId: UUID) {
        val profile = accessor.getProfileOrNull(profileId)
        if (profile == null) {
            player.sendLangMsg(MSG.PROFILE_NOT_FOUND, "comment" to "Profile not found. Report to admin. PlayerId: $playerId, ProfileId: $profileId")
            return
        }
        DeleteConfirmGUI(player, profile).open()
    }



    companion object {

        class DeleteConfirmGUI(player: Player, val profile: PlayerProfile) : ConfirmationGUI(player) {

            override fun getTitle(): String {
                return player.getLangMsg(MSG.PROFILE_GUI_CONFIRM_TITLE).text
            }

            override fun getMainItem(): ItemStack {
                return profile.getIcon(player)
            }

            override fun onConfirm(event: InventoryClickEvent) {
                profile.delete()
                closeAndFinish()
                player.sendLangMsg(MSG.PROFILE_DELETED, profile.getFinalPlaceholder(player))
            }
        }

        val CREATE_PROFILE_FREE = ItemPattern.from(Material.BARRIER)
            .setDisplayName(MSG.PROFILE_CREATE_ICON_DISPLAY)
            .setTouchable(false)
            .setTag("isProfileAdd", true)
            .setTag("profileAdd", "FREE")

        val CREATE_PROFILE_GOLD = ItemPattern.from(Material.BARRIER)
            .setDisplayName(MSG.PROFILE_CREATE_ICON_DISPLAY)
            .setTouchable(false)
            .setTag("isProfileAdd", true)
            .setTag("requireDonate", true)
            .setTag("requiredDonate", "Gold")

        val CREATE_PROFILE_PREMIUM = ItemPattern.from(Material.BARRIER)
            .setDisplayName(MSG.PROFILE_CREATE_ICON_DISPLAY)
            .setTouchable(false)
            .setTag("isProfileAdd", true)
            .setTag("requireDonate", true)
            .setTag("requiredDonate", "Premium")

        val CREATE_PROFILE_MVP = ItemPattern.from(Material.BARRIER)
            .setDisplayName(MSG.PROFILE_CREATE_ICON_DISPLAY)
            .setTouchable(false)
            .setTag("isProfileAdd", true)
            .setTag("requireDonate", true)
            .setTag("requiredDonate", "MVP")
    }
}
