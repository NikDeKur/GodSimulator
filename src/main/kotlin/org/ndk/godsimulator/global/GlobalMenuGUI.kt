package org.ndk.godsimulator.global

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.shop.ShopMainGUI
import org.ndk.minecraft.extension.getBooleanTag
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.extension.getStringTag
import org.ndk.minecraft.extension.isAir
import org.ndk.minecraft.gui.GUI
import org.ndk.minecraft.gui.GUIFlag
import org.ndk.minecraft.item.ItemPattern


class GlobalMenuGUI(player: Player) : GUI(player, 54) {
    override fun getTitle(): String {
        return player.getLangMsg(MSG.PLAYER_MENU_GUI_TITLE).text
    }

    override val flags: Set<GUIFlag> = setOf(GUIFlag.CANNOT_TAKE, GUIFlag.CANNOT_PUT)

    override fun beforeOpen() {
        inventory.setItem(20, SHOP_ITEM.build(player))
    }

    override fun onClick(event: InventoryClickEvent) {
        val item = event.currentItem ?: return
        if (item.type.isAir) return
        if (item.getBooleanTag("isMenuPart") != true) return
        val part = item.getStringTag("part") ?: return
        val player = event.whoClicked as? Player ?: return
        when (part) {
            "shop" -> ShopMainGUI(player).open()
        }
    }

    companion object {
        val SHOP_ITEM = ItemPattern.from(Material.CHEST)
            .setDisplayName(MSG.PLAYER_MENU_SHOP_ITEM_DISPLAY)
            .setTag("isMenuPart", true)
            .setTag("part", "shop")
            .setTouchable(false)
    }
}

