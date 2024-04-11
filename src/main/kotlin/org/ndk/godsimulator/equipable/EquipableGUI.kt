package org.ndk.godsimulator.equipable

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.ndk.godsimulator.equipable.inventory.EquipableInventory
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.menu.GlobalMenu
import org.ndk.godsimulator.menu.GlobalMenu.Companion.checkGoBack
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profile
import org.ndk.godsimulator.utils.SimPattern
import org.ndk.klib.sub
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.gui.PagedGUI
import org.ndk.minecraft.item.Patterns
import java.util.*

/**
 * Note: After equip-unEquip an equipable, the order of equipable in "all" section will not be updated
 *
 * Because of no changes to the original LinkedHashMap [EquipableInventory.all] in [EquipableInventory]
 */
abstract class EquipableGUI(player: Player) : PagedGUI(player, 54) {

    override val nextArrowPos = NEXT_ARROW_POS
    override val previousArrowPos = PREVIOUS_ARROW_POS

    override val previousArrow = SimPattern.ARROW_PREVIOUS.build(player)
    override val nextArrow = SimPattern.ARROW_NEXT.build(player)

    val profile = player.profile
    override val startInventoryFrom = START_INVENTORY_FROM

    abstract var equipInventory: EquipableInventory<*>


    val equipped: ArrayList<ItemStack>
        get() {
            val all = equipInventory.equipped.values
            val equipped = ArrayList<ItemStack>()
            for (value in all) {
                equipped.add(value.getIcon(player))
            }
            return equipped
        }

    override val content: ArrayList<ItemStack>
        get() {
            val all = equipInventory.all.values
            val equipped = equipInventory.equipped
            val content = ArrayList<ItemStack>()
            for (value in all) {
                if (equipped.contains(value.id)) continue
                content.add(value.getIcon(player))
            }
            return content
        }


    override fun changeInventory(inventory: Inventory) {
        inventory.setItem(0, GlobalMenu.ITEM_GO_BACK.build(player))
        inventory.setItem(8, Patterns.EMPTY_SLOT)
        inventory.setRow(2, Patterns.EMPTY_SLOT)

        val equipped = equipped.sub(0, 7)
        inventory.addItems(equipped)
    }




    override fun whenClick(event: InventoryClickEvent) {
        val item = event.currentItem

        event.cancel()
        val equipableId = item.getStringTag("equipableId")
        if (equipableId != null) {
            if (event.slot < START_INVENTORY_FROM) {
                onUnEquip(UUID.fromString(equipableId))
            } else {
                onEquip(UUID.fromString(equipableId))
            }
        } else {
            checkGoBack(item)
        }

    }

    fun onUnEquip(obj: UUID) {
        if (equipInventory.unEquip(obj))
            update()
    }

    fun onEquip(obj: UUID) {
        if (equipInventory.equip(obj))
            update()
    }

    companion object {
        const val PREVIOUS_ARROW_POS = 45
        const val NEXT_ARROW_POS = 53
        const val START_INVENTORY_FROM = 18

        fun items(player: Player) = object : EquipableGUI(player) {
            override var equipInventory: EquipableInventory<*> = profile.items

            override fun getTitle(): String {
                return player.getLangMsg(MSG.GLOBAL_MENU_ITEMS_TITLE).text
            }
        }

        fun pets(player: Player) = object : EquipableGUI(player) {
            override var equipInventory: EquipableInventory<*> = profile.pets

            override fun getTitle(): String {
                return player.getLangMsg(MSG.GLOBAL_MENU_PETS_TITLE).text
            }
        }
    }
}