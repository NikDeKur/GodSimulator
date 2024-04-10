package org.ndk.godsimulator.extension

import org.bukkit.inventory.ItemStack
import org.ndk.minecraft.extension.setTag
import org.ndk.minecraft.item.ItemPattern

/**
 * Set the texture of the item.
 *
 * Uses to set the texture of the item for displaying optifine cit texture.
 *
 * nbt.texture = value
 */
fun ItemPattern.setTexture(texture: String): ItemPattern {
   this.setTag("texture", texture)
    return this
}

/**
 * Set the texture of the item.
 *
 * Uses to set the texture of the item for displaying optifine cit texture.
 *
 * nbt.texture = value
 */
fun ItemStack.setTexture(texture: String): ItemStack {
    this.setTag("texture", texture)
    return this
}