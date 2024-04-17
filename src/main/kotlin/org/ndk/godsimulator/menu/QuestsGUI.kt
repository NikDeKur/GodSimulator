package org.ndk.godsimulator.menu

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.extension.isEmpty
import org.ndk.minecraft.extension.setRow
import org.ndk.minecraft.gui.PagedGUI
import org.ndk.minecraft.item.Patterns

class QuestsGUI(player: Player) : PagedGUI(player, 54) {


    override val content: List<ItemStack>
        get() {
            return emptyList()
        }

    override fun beforeOpen() {
        inventory.setRow(1, Patterns.EMPTY_SLOT)
        inventory.setRow(6, Patterns.EMPTY_SLOT)

        inventory.setItem(0, GlobalMenu.ITEM_GO_BACK.build(player))
    }

    override fun onClick(event: InventoryClickEvent) {
        val item = event.currentItem
        if (item.isEmpty()) return
    }

    override fun getTitle(): String {
        return player.getLangMsg(MSG.GLOBAL_MENU_QUESTS_TITLE).text
    }
}