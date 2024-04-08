package org.ndk.godsimulator.quest.gui

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.gui.PagedGUI

class QuestBookGUI(player: Player) : PagedGUI(player, 54) {
    override val content: List<ItemStack>
        get() {
            return emptyList()
        }

    override fun getTitle(): String {
        return player.getLangMsg(MSG.QUEST_BOOK_TITLE).text
    }
}