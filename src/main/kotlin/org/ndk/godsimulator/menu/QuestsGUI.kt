package org.ndk.godsimulator.menu

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.gui.PagedGUI

class QuestsGUI(player: Player) : PagedGUI(player, 54) {


    override val content: List<ItemStack>
        get() {
            return emptyList()
        }

    override fun getTitle(): String {
        return player.getLangMsg(MSG.GLOBAL_MENU_QUESTS_TITLE).text
    }
}