package org.ndk.godsimulator.equipable.impl

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.equipable.BuffsEquipable
import org.ndk.godsimulator.equipable.Rarity
import org.ndk.godsimulator.equipable.inventory.EquipableInventory
import org.ndk.godsimulator.equipable.type.EquipableType
import org.ndk.godsimulator.equipable.type.EquipableTypesManager
import org.ndk.godsimulator.extension.readMSGHolderOrThrow
import org.ndk.godsimulator.extension.readRarityOrThrow
import org.ndk.godsimulator.extension.readStats
import org.ndk.godsimulator.extension.setTexture
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.rpg.buff.ImaginaryBuffsList
import org.ndk.minecraft.extension.forEachSectionSafe
import org.ndk.minecraft.item.ItemPattern
import org.ndk.minecraft.language.MSGHolder
import org.ndk.minecraft.plugin.ServerPlugin
import java.util.*

typealias Pet = BuffsEquipable<PetType>

data class PetType(
    override val manager: PetsManager,
    override val hierarchy: Int,
    override val id: String,
    override val nameMSG: MSGHolder,
    val rarity: Rarity,
    val buffs: ImaginaryBuffsList,
) : EquipableType<PetType> {
    override fun getIcon(player: Player): ItemStack {
        val placeholder = getFinalPlaceholder(player)
        return ItemPattern.from(Material.BARRIER)
            .setDisplayName(MSG.PET_ICON_DISPLAY)
            .setTexture(id)
            .setHideAttributes(true)
            .setTouchable(false)
            .apply {
                val lore = GodSimulator.rpgManager.formatToLore(buffs, player)
                if (lore.isEmpty()) return@apply
                lore.add(0, "")
                this.setLore(lore)
            }
            .setTag("equipableType", id)
            .setTag("petType", id)
            .build(player, placeholder)
    }


    override fun newEquipable(id: UUID): Pet {
        return Pet(this, id, buffs)
    }
}


class PetsManager : EquipableTypesManager<PetType>() {
    override val qualifier: String = "pet"
    override fun onLoad(plugin: ServerPlugin) {
        val config = plugin.configsManager.load("pets")
        config.forEachSectionSafe {
            val id = it.name
            val hierarchy = this.types.size + 1
            val name = it.readMSGHolderOrThrow("name")
            val rarity = it.readRarityOrThrow("rarity")
            val buffs = it.readStats("stats")

            val petType = PetType(this, hierarchy, id, name, rarity, buffs)
            addType(petType)
        }
    }

    override fun getInventory(profile: PlayerProfile): EquipableInventory<PetType> {
        return profile.pets
    }
}

class PetsInventory(profile: PlayerProfile) : EquipableInventory<PetType>(profile) {
    override val equipLimit by profile::petsEquipLimit
    override val allLimit by profile::petsLimit
}