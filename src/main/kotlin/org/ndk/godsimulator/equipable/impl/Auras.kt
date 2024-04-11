package org.ndk.godsimulator.equipable.impl

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.buying.Currency
import org.ndk.godsimulator.buying.Wallet
import org.ndk.godsimulator.buying.WalletImpl
import org.ndk.godsimulator.equipable.BuffsEquipable
import org.ndk.godsimulator.equipable.inventory.EquipableInventory
import org.ndk.godsimulator.equipable.inventory.SingleEquipableInventory
import org.ndk.godsimulator.equipable.type.BuyableEquipableType
import org.ndk.godsimulator.equipable.type.EquipableTypesManager
import org.ndk.godsimulator.extension.readMSGHolderOrThrow
import org.ndk.godsimulator.extension.readStats
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.rpg.buff.ImaginaryBuffsList
import org.ndk.godsimulator.shop.ShopGUI
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.item.ItemPattern
import org.ndk.minecraft.language.MSGHolder
import org.ndk.minecraft.plugin.ServerPlugin
import java.math.BigInteger
import java.util.*


typealias Aura = BuffsEquipable<AuraType>

data class AuraType(
    override val manager: AurasManager,
    override val hierarchy: Int,
    override val id: String,
    val icon: Material,
    override val nameMSG: MSGHolder,
    val priceSouls: BigInteger,
    val buffs: ImaginaryBuffsList,
) : BuyableEquipableType<AuraType> {
    override fun getIcon(player: Player): ItemStack {
        val placeholder = getFinalPlaceholder(player)
        return ItemPattern.from(icon)
            .setDisplayName(MSG.AURA_ICON_DISPLAY)
            .setTouchable(false)
            .setHideAttributes(true)
            .apply {
                val lore = GodSimulator.rpgManager.formatToLore(buffs, player)
                if (lore.isEmpty()) return@apply
                lore.add(0, "")
                this.setLore(lore)
            }
            .setTag("equipableType", id)
            .setTag("auraType", id)
            .build(player, placeholder)
    }

    override val price: Wallet = WalletImpl().apply {
        setBalance(Currency.SOULS, priceSouls)
    }

    override val buyable: Boolean
        get() = priceSouls > BigInteger.ZERO

    override fun newEquipable(id: UUID): Aura {
        return Aura(this, id, buffs)
    }

    override fun beforeBuy(profile: PlayerProfile, silent: Boolean): Boolean {
        val player = profile.onlinePlayer

        if (profile.auras.has(this)) {
            if (!silent)
                player?.sendLangMsg(MSG.AURA_BUY_ALREADY_HAVE, getFinalPlaceholder(player))
            return false
        }

        return true
    }


    override fun afterBuy(profile: PlayerProfile, silent: Boolean) {
        val player = profile.onlinePlayer
        if (!silent)
            player?.sendLangMsg(MSG.AURA_BUY_SUCCESS, getFinalPlaceholder(player))
    }

}

class AurasManager : EquipableTypesManager<AuraType>() {
    override val qualifier: String = "aura"
    override fun onLoad(plugin: ServerPlugin) {
        val config = plugin.configsManager.load("auras")
        config.forEachSectionSafe {
            val id = it.name
            val hierarchy = this.types.size + 1
            val icon = it.readMaterialOrThrow("icon")
            val name = it.readMSGHolderOrThrow("name")
            val price = it.readBigInteger("price") ?: BigInteger.ZERO
            val buffs = it.readStats("stats")

            val type = AuraType(this, hierarchy, id, icon, name, price, buffs)
            addType(type)
        }
    }

    override fun getInventory(profile: PlayerProfile): EquipableInventory<AuraType> {
        return profile.auras
    }
}

class AurasInventory(profile: PlayerProfile) : SingleEquipableInventory<AuraType>(profile) {
    override val allLimit = -1
}

class AurasShopGUI(
    player: Player
) : ShopGUI<AuraType>(
    player,
    GodSimulator.equipableManager.auras
) {
    override fun getTitle(): String {
        return player.getLangMsg(MSG.AURAS_SHOP_GUI_TITLE).text
    }
}