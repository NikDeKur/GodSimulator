package org.ndk.godsimulator.utils

import org.bukkit.Material
import org.ndk.godsimulator.extension.setTexture
import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.extension.Patterns
import org.ndk.minecraft.item.ItemPattern

object SimPattern {

    val CONFIRM = ItemPattern.from(Material.BARRIER)
        .setDisplayName(MSG.CONFIRM_ITEM_DISPLAY)
        .setLore(MSG.CONFIRM_ITEM_LORE)
        .setTexture("accept")
        .setTouchable(false)

    val CANCEL = ItemPattern.from(Material.BARRIER)
        .setDisplayName(MSG.CANCEL_ITEM_DISPLAY)
        .setLore(MSG.CANCEL_ITEM_LORE)
        .setTexture("cancel")
        .setTouchable(false)

    val ARROW_NEXT = Patterns.ARROW_NEXT.clone()
        .setMaterial(Material.BARRIER)
        .setTexture("arrow_right")

    val ARROW_PREVIOUS = Patterns.ARROW_PREVIOUS.clone()
        .setMaterial(Material.BARRIER)
        .setTexture("arrow_left")
}