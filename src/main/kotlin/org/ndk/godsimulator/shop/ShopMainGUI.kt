package org.ndk.godsimulator.shop

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.gui.GUI
import org.ndk.minecraft.gui.GUIFlag

class ShopMainGUI(player: Player) : GUI(player, 9) {

    override fun getTitle(): String {
        return player.getLangMsg(MSG.SHOP_GUI_TITLE).text
    }

    override val flags: Set<GUIFlag> = setOf(GUIFlag.CANNOT_TAKE, GUIFlag.CANNOT_PUT)


    override fun beforeOpen() {
        inventory.setItem(4, BAG_ITEM)
    }

    override fun onClick(event: InventoryClickEvent) {
        val item = event.currentItem ?: return
        if (item.type == Material.AIR) return
        val shop = item.getStringTag("shop") ?: return
        val player = event.whoClicked as? Player ?: return
        GodSimulator.shopManager
            .shopGUIs[shop]
            ?.getConstructor(Player::class.java)
            ?.newInstance(player)
            ?.open()
    }

    companion object {
        val BAG_ITEM = ItemStack(Material.CHEST)
            .setDisplayName("&6Сумка")
            .setLore("", "&7Позволяет хранить предметы")
            .setTag("shop", "bag")
            .setTouchable(false)

        val BACK_ITEM = Patterns.ARROW_PREVIOUS.clone()
            .setDisplayName(MSG.SHOP_BACK_DISPLAY)
            .setLore(MSG.SHOP_BACK_LORE)
            .setTag("isShopBack", true)
            .setTouchable(false)
    }
}