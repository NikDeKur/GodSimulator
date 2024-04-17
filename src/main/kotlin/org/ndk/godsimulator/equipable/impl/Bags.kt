package org.ndk.godsimulator.equipable.impl

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.ndk.godsimulator.economy.currency.Currency
import org.ndk.godsimulator.economy.wallet.Wallet
import org.ndk.godsimulator.economy.wallet.WalletImpl
import org.ndk.godsimulator.equipable.Equipable
import org.ndk.godsimulator.equipable.EquipableManager
import org.ndk.godsimulator.equipable.inventory.EquipableInventory
import org.ndk.godsimulator.equipable.inventory.SingleEquipableInventory
import org.ndk.godsimulator.equipable.type.BuyableEquipableType
import org.ndk.godsimulator.equipable.type.EquipableTypesManager
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.shop.ShopGUI
import org.ndk.klib.Constants
import org.ndk.klib.firstValue
import org.ndk.klib.orElseThrow
import org.ndk.klib.toBeautifulString
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.item.ItemPattern
import org.ndk.minecraft.language.MSGHolder
import org.ndk.minecraft.plugin.ServerPlugin
import java.math.BigInteger
import java.util.*


typealias Bag = Equipable<BagType>

data class BagType(
    override val manager: BagsManager,
    override val hierarchy: Int,
    override val id: String,
    override val nameMSG: MSGHolder,
    val size: BigInteger,
    val priceCoins: BigInteger
) : BuyableEquipableType<BagType> {


    override fun getIcon(player: Player): ItemStack {
        val placeholder = getFinalPlaceholder(player)
        return PATTERN
            .build(player, placeholder)
            .setTag("equipableType", id)
            .setTag("bagType", id)
    }

    override val placeholderMap: MutableMap<String, Any>
        get() = super.placeholderMap.also {
            it["price"] = priceCoins.toBeautifulString()
            it["size"] =
                if (isInfinity || isAutoSell) "∞"
                else size.toBeautifulString()
        }

    val isInfinity = size == Constants.BIGINT_MINUS1
    val isAutoSell = size == Constants.BIGINT_MINUS2
    val isDefault: Boolean
        get() = this == manager.defaultBagType

    override val price: Wallet = WalletImpl().apply {
        setBalance(Currency.COINS, priceCoins)
    }

    override val buyable: Boolean
        get() = priceCoins > BigInteger.ZERO && !isDefault




    override fun beforeBuy(profile: PlayerProfile, silent: Boolean): Boolean {
        val current = profile.bags.bag
        val currentType = current.type

        val online = profile.onlinePlayer

        if (profile.bags.has(this)) {
            if (!silent)
                online?.sendLangMsg(MSG.BAG_BUY_ALREADY_HAVE, getFinalPlaceholder(online))
            return false

        } else if (currentType.isInfinity && !this.isAutoSell) {
            if (!silent)
                online?.sendLangMsg(MSG.BAG_BUY_ALREADY_HAS_INFINITY)
            return false

        } else if (currentType.isAutoSell) {
            if (!silent)
                online?.sendLangMsg(MSG.BAG_BUY_ALREADY_HAS_AUTOSELL)
            return false
        }

        return true
    }

    override fun afterBuy(profile: PlayerProfile, silent: Boolean) {
        val player = profile.onlinePlayer
        if (player != null && !silent) {
            player.sendLangMsg(MSG.BAG_BUY_SUCCESS, getFinalPlaceholder(player))
        }
    }

    override fun newEquipable(id: UUID): Bag {
        return Equipable(this, id)
    }

    companion object {
        val PATTERN = ItemPattern.from(Material.CHEST)
            .setHideAttributes(true)
            .setDisplayName(MSG.BAG_ICON_DISPLAY)
            .setLore(MSG.BAG_ICON_LORE)
            .setTag("isBag", true)
            .setTouchable(false)
    }
}


class BagsManager : EquipableTypesManager<BagType>() {

    lateinit var defaultBagType: BagType
    lateinit var defaultBag: Bag
    override val qualifier: String = "bag"

    override fun onLoad(plugin: ServerPlugin) {
        val config = plugin.configsManager.load("bags")
        val typesSection = config.getSection("types").orElseThrow {
            Exception("No bags types provided. BagsManager cannot be loaded without at least 1 bag type")
        }

        typesSection.forEachSectionSafe {
            val id = it.name
            val hierarchy = it.getIntOr("hierarchy", types.size + 1)!!
            val name = it.readMSGHolderOrThrow("name")
            val size = it.readBigIntegerOrThrow("size")
            val price = it.readBigInteger("price", Constants.BIGINT_MINUS1)!!
            val type = BagType(this, hierarchy, id, name, size, price)
            addType(type)
        }

        val default = config.getString("default")!!
        defaultBagType = types[default] ?: types.firstValue()
        defaultBag = defaultBagType.newEquipable()
    }
    override fun getInventory(profile: PlayerProfile): EquipableInventory<BagType> {
        return profile.bags
    }
}

class BagsInventory(profile: PlayerProfile) : SingleEquipableInventory<BagType>(profile) {
    override val allLimit = -1

    val bag: Bag
        get() = item ?: EquipableManager.bags.defaultBag
}

class BagsShopGUI(
    player: Player
) : ShopGUI<BagType>(
    player,
    EquipableManager.bags
) {

    override fun getTitle(): String {
        return player.getLangMsg(MSG.BAGS_SHOP_GUI_TITLE).text
    }
}

