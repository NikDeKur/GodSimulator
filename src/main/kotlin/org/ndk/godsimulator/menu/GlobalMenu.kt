package org.ndk.godsimulator.menu

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.ndk.godsimulator.equipable.EquipableGUI
import org.ndk.godsimulator.extension.setTexture
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.gui.GUI
import org.ndk.minecraft.item.ItemPattern

class GlobalMenu(player: Player) : GUI(player, 54) {
    override fun getTitle(): String {
        return player.getLangMsg(MSG.GLOBAL_MENU_TITLE).text
    }

    override fun beforeOpen() {
        inventory.setRow(1, Patterns.EMPTY_SLOT)
        inventory.setRow(6, Patterns.EMPTY_SLOT)
        inventory.setColumn(1, Patterns.EMPTY_SLOT)
        inventory.setColumn(9, Patterns.EMPTY_SLOT)


        inventory.setItem(13, ITEM_PROFILES.build(player))

        inventory.setItem(21, ITEM_SKILLS.build(player))
        inventory.setItem(22, ITEM_REBIRTH.build(player))
        inventory.setItem(23, ITEM_QUESTS.build(player))

        inventory.setItem(30, ITEM_ITEMS.build(player))
        inventory.setItem(31, ITEM_PETS.build(player))
        inventory.setItem(33, ITEM_STATS.build(player))
        inventory.setItem(34, ITEM_SETTINGS.build(player))
    }

    override fun onClick(event: InventoryClickEvent) {
        val item = event.currentItem ?: return
        if (item.type == Material.AIR) return
        val type = item.getStringTag("type") ?: return
        when (type) {
            "profiles" -> {
                ProfilesGUI(player).open()
            }
            "skills" -> {
                SkillsGUI(player).open()
            }
            "rebirth" -> {
                RebirthGUI(player).open()
            }
            "quests" -> {
                QuestsGUI(player).open()
            }
            "items" -> {
                EquipableGUI.items(player).open()
            }
            "pets" -> {
                EquipableGUI.pets(player).open()
            }
            "stats" -> {
                StatsGUI(player).open()
            }
            "settings" -> {
                SettingsGUI(player).open()
            }
        }
    }


    companion object {

        val ITEM_PROFILES = ItemPattern.from(Material.SKULL)
            .setDurability(3)
            .setDisplayName(MSG.GLOBAL_MENU_PROFILE_ITEM_DISPLAY)
            .setLore(MSG.GLOBAL_MENU_PROFILE_ITEM_LORE)
            .setTag("type", "profiles")

        val ITEM_SKILLS = ItemPattern.from(Material.BARRIER)
            .setDisplayName(MSG.GLOBAL_MENU_SKILLS_ITEM_DISPLAY)
            .setLore(MSG.GLOBAL_MENU_SKILLS_ITEM_LORE)
            .setTexture("skill")
            .setTag("type", "skills")

        val ITEM_REBIRTH = ItemPattern.from(Material.NETHER_STAR)
            .setDisplayName(MSG.GLOBAL_MENU_REBIRTH_ITEM_DISPLAY)
            .setLore(MSG.GLOBAL_MENU_REBIRTH_ITEM_LORE)
            .setTag("type", "rebirth")

        val ITEM_QUESTS = ItemPattern.from(Material.BOOK)
            .setDisplayName(MSG.GLOBAL_MENU_QUESTS_ITEM_DISPLAY)
            .setLore(MSG.GLOBAL_MENU_QUESTS_ITEM_LORE)
            .setTag("type", "quests")

        val ITEM_ITEMS = ItemPattern.from(Material.STICK)
            .setDisplayName(MSG.GLOBAL_MENU_ITEMS_ITEM_DISPLAY)
            .setLore(MSG.GLOBAL_MENU_ITEMS_ITEM_LORE)
            .setTag("type", "items")

        val ITEM_PETS = ItemPattern.from(Material.STICK)
            .setDisplayName(MSG.GLOBAL_MENU_PETS_ITEM_DISPLAY)
            .setLore(MSG.GLOBAL_MENU_PETS_ITEM_LORE)
            .setTag("type", "pets")

        val ITEM_STATS = ItemPattern.from(Material.IRON_SWORD)
            .setDisplayName(MSG.GLOBAL_MENU_STATISTICS_ITEM_DISPLAY)
            .setLore(MSG.GLOBAL_MENU_STATISTICS_ITEM_LORE)
            .setTag("type", "stats")

        val ITEM_SETTINGS = ItemPattern.from(Material.BARRIER)
            .setDisplayName(MSG.GLOBAL_MENU_SETTINGS_ITEM_DISPLAY)
            .setLore(MSG.GLOBAL_MENU_SETTINGS_ITEM_LORE)
            .setTexture("settings")
            .setTag("type", "settings")
    }
}