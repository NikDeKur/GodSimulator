package org.ndk.godsimulator.god


import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.ndk.godsimulator.GodSimulator.Companion.godsManager
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.language.Quick
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profile
import org.ndk.minecraft.extension.cancel
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.extension.getStringTag
import org.ndk.minecraft.gui.GUI
import org.ndk.minecraft.gui.GUIFlag

open class GodSelectGUI(player: Player) : GUI(player, 27) {

    override fun getTitle(): String {
        return player.getLangMsg(MSG.GOD_SELECT_TITLE).text
    }

    override val flags: Set<GUIFlag> = setOf(GUIFlag.CANNOT_TAKE, GUIFlag.CANNOT_PUT)

    override fun beforeOpen() {
        godsManager.gods.forEach { (_, god) ->
            if (god.isSelectable)
                inventory.addItem(god.getIcon(player))
        }
    }


    override fun onClick(event: InventoryClickEvent) {
        event.cancel()
        if (!event.isLeftClick) return
        val item = event.currentItem ?: return

        val player = event.whoClicked as? Player ?: return
        val godId = item.getStringTag("god") ?: return
        val god = godsManager.getGod(godId)
        if (god == null) {
            Quick.internalError(player, "(godId: $godId)")
            return
        }

        onGodSelect(god)
    }

    open fun onGodSelect(god: God) {
        player.profile.setGod(god)
        closeAndFinish()
    }
}