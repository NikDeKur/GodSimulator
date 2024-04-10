package org.ndk.godsimulator.utils

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.ndk.minecraft.gui.ConfirmationGUI

abstract class ConfirmationGUI(player: Player) : ConfirmationGUI(player) {

    override fun getCancelItem(): ItemStack {
        return SimPattern.CONFIRM.build(player)
    }

    override fun getConfirmItem(): ItemStack {
        return SimPattern.CANCEL.build(player)
    }
}