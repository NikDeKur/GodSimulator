package org.ndk.godsimulator.menu

import org.bukkit.entity.Player
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.gui.GUI

class RebirthGUI(player: Player) : GUI(player, 27) {
    override fun getTitle(): String {
        return player.getLangMsg(MSG.GLOBAL_MENU_REBIRTH_TITLE).text
    }
}