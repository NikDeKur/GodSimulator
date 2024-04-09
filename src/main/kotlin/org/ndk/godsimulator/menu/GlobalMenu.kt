package org.ndk.godsimulator.menu

import org.bukkit.entity.Player
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.gui.GUI

class GlobalMenu(player: Player) : GUI(player, 54) {
    override fun getTitle(): String {
        return player.getLangMsg(MSG.GLOBAL_MENU_TITLE).text
    }

    override fun beforeOpen() {

    }
}