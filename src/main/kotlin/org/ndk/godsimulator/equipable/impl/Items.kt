package org.ndk.godsimulator.equipable.impl

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.ndk.godsimulator.equipable.BuffsEquipable
import org.ndk.godsimulator.equipable.inventory.EquipableInventory
import org.ndk.godsimulator.equipable.type.EquipableType
import org.ndk.godsimulator.equipable.type.EquipableTypesManager
import org.ndk.godsimulator.extension.readStats
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.rpg.RPGManager
import org.ndk.godsimulator.rpg.buff.ImaginaryBuffsList
import org.ndk.minecraft.extension.forEachSectionSafe
import org.ndk.minecraft.extension.readMSGHolderOrThrow
import org.ndk.minecraft.extension.readMaterialOrThrow
import org.ndk.minecraft.item.ItemPattern
import org.ndk.minecraft.language.MSGHolder
import org.ndk.minecraft.plugin.ServerPlugin
import java.util.*

typealias Item = BuffsEquipable<ItemType>

data class ItemType(
    override val manager: ItemsManager,
    override val hierarchy: Int,
    override val id: String,
    val icon: Material,
    override val nameMSG: MSGHolder,
    val buffs: ImaginaryBuffsList,
) : EquipableType<ItemType> {
    override fun getIcon(player: Player): ItemStack {
        val placeholder = getFinalPlaceholder(player)
        return ItemPattern.from(icon)
            .setDisplayName(MSG.PET_ICON_DISPLAY)
            .setTouchable(false)
            .setHideAttributes(true)
            .apply {
                val lore = RPGManager.formatToLore(buffs, player)
                if (lore.isEmpty()) return@apply
                lore.add(0, "")
                this.setLore(lore)
            }
            .setTag("equipableType", id)
            .setTag("itemType", id)
            .build(player, placeholder)
    }


    override fun newEquipable(id: UUID): Item {
        return Item(this, id, buffs)
    }
}

class ItemsManager : EquipableTypesManager<ItemType>() {
    override val qualifier: String = "item"
    override fun onLoad(plugin: ServerPlugin) {
        val config = plugin.configsManager.load("items")
        config.forEachSectionSafe {
            val id = it.name
            val hierarchy = this.types.size + 1
            val icon = it.readMaterialOrThrow("icon")
            val name = it.readMSGHolderOrThrow("name")
            val buffs = it.readStats("stats")

            val itemType = ItemType(this, hierarchy, id, icon, name, buffs)
            addType(itemType)
        }
    }

    override fun getInventory(profile: PlayerProfile): EquipableInventory<ItemType> {
        return profile.items
    }
}

class ItemsInventory(profile: PlayerProfile) : EquipableInventory<ItemType>(profile) {
    override val equipLimit by profile::itemsEquipLimit
    override val allLimit = -1
}