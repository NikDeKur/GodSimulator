package org.ndk.godsimulator.menu

import org.bukkit.entity.Player
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.gui.GUI

class SettingsGUI(player: Player) : GUI(player, 27) {
    override fun getTitle(): String {
        return player.getLangMsg(MSG.GLOBAL_MENU_SETTINGS_TITLE).text
    }
}