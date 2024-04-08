package org.ndk.godsimulator.lib

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.gui.ConfirmationGUI
import org.ndk.minecraft.item.ItemPattern

abstract class ConfirmationGUI(player: Player) : ConfirmationGUI(player) {

    override fun getCancelItem(): ItemStack {
        return cancelItem.build(player)
    }

    override fun getConfirmItem(): ItemStack {
        return confirmItem.build(player)
    }

    companion object {
        val confirmItem = ItemPattern.from(Material.GREEN_GLAZED_TERRACOTTA)
            .setDisplayName(MSG.CONFIRM_ITEM_DISPLAY)
            .setLore(MSG.CONFIRM_ITEM_LORE)
            .setTouchable(false)

        val cancelItem = ItemPattern.from(Material.RED_GLAZED_TERRACOTTA)
            .setDisplayName(MSG.CANCEL_ITEM_DISPLAY)
            .setLore(MSG.CANCEL_ITEM_LORE)
            .setTouchable(false)
    }
}