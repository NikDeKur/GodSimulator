package org.ndk.godsimulator.shop

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.ndk.godsimulator.equipable.type.BuyableEquipableType
import org.ndk.godsimulator.equipable.type.EquipableTypesManager
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.language.Quick
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profile
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.gui.PagedGUI
import org.ndk.minecraft.item.Patterns

abstract class ShopGUI<T : BuyableEquipableType<T>>(
    player: Player,
    val manager: EquipableTypesManager<T>
) : PagedGUI(player, 54)  {

    override val content: List<ItemStack>
        get() {
            val list = ArrayList<ItemStack>()
            manager.types.values.sortedBy { it.hierarchy }.forEach {
                if (!it.buyable) return@forEach
                val item = getIcon(it)
                list.add(item)
            }
            return list
        }

    fun getIcon(type: T): ItemStack {
        val icon = type.getIcon(player)
        val inventory = manager.getInventory(player.profile)

        val has = inventory.has(type)
        val equipped = has && inventory.isEquipped(type)

        val lore = ArrayList<String>(2)
        lore.add("")

        if (!has) {
            Quick.priceMessage(player, type.price).split("\n").forEach {
                lore.add(it)
            }
            lore.add("")
        }

        lore.add(
            player.getLangMsg(
                if (equipped)
                    MSG.SHOP_EQUIPPED
                else if (has)
                    MSG.SHOP_EQUIP
                else
                    MSG.SHOP_BUY
            ).text
        )

        icon.addLore(lore)
        return icon
    }

    override fun whenClick(event: InventoryClickEvent) {
        val item = event.currentItem
        if (item.type == Material.AIR) return
        val equipId = item.getStringTag("equipableType")
        val player = event.whoClicked as? Player ?: return
        event.cancel()
        if (equipId != null) {
            val type = manager.getType(equipId)
            val profile = player.profile
            if (type == null) {
                player.sendLangMsg(MSG.ERROR_FINDING_ITEM)
                update()
                return
            }
            if (
                if (event.isLeftClick)
                    onBuy(profile, type)
                else if (event.isRightClick)
                    onEquip(profile, type)
                else false
            ) update()

        }
    }

    fun onBuy(profile: PlayerProfile, type: T): Boolean {
        if (manager.getInventory(profile).has(type)) return false
        profile.buy(type)
        return true
    }

    fun onEquip(profile: PlayerProfile, type: T): Boolean {
        val inventory = manager.getInventory(profile)
        if (!inventory.has(type)) return false
        val item = inventory.get(type).firstOrNull() ?: return false
        inventory.equip(item)
        return true
    }

    override fun changeInventory(inventory: Inventory) {
        inventory.setRow(1, Patterns.EMPTY_SLOT)
        inventory.setRow(6, Patterns.EMPTY_SLOT)
    }

    abstract fun clone(): ShopGUI<T>
}