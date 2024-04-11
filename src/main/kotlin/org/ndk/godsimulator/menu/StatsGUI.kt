package org.ndk.godsimulator.menu

import org.bukkit.entity.Player
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.gui.GUI
import org.ndk.minecraft.gui.GUIFlag

class StatsGUI(player: Player) : GUI(player, 27) {

    override val flags: Set<GUIFlag> = setOf(GUIFlag.CANNOT_TAKE, GUIFlag.CANNOT_PUT)

    override fun getTitle(): String {
        return player.getLangMsg(MSG.GLOBAL_MENU_STATISTICS_TITLE).text
    }
}