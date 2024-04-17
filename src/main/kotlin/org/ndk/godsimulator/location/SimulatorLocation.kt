package org.ndk.godsimulator.location

import com.sk89q.worldedit.regions.Region
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import org.ndk.global.interfaces.Snowflake
import org.ndk.global.spatial.Point
import org.ndk.godsimulator.economy.Buyable
import org.ndk.godsimulator.economy.currency.Currency
import org.ndk.godsimulator.economy.wallet.Wallet
import org.ndk.godsimulator.economy.wallet.WalletImpl
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.selling.SellZone
import org.ndk.godsimulator.wobject.WorldRegion
import org.ndk.godsimulator.world.SimulatorWorld
import org.ndk.klib.toBeautifulString
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.extension.sendLangMsg
import org.ndk.minecraft.item.ItemPattern
import org.ndk.minecraft.language.MSGHolder
import java.math.BigInteger

class SimulatorLocation(
    override val id: String,
    override val nameMSG: MSGHolder,
    override val world: SimulatorWorld,
    val region: Region,
    val priceCoins: BigInteger,
    val material: Material
) : Region by region, Snowflake<String>, MSGNameHolder, WorldRegion, Buyable {

    override val buyable: Boolean
        get() = priceCoins > BigInteger.ZERO

    override val price: Wallet = WalletImpl().apply {
        setBalance(Currency.COINS, priceCoins)
    }

    override val defaultPhName: String = "location"
    override val placeholderMap: MutableMap<String, Any>
        get() = super.placeholderMap.also {
            it["price"] = priceCoins.toBeautifulString()
        }

    override fun getIcon(player: Player): ItemStack {
        return ItemPattern.from(material)
            .setDisplayName(MSG.LOCATION_UNLOCK_ITEM_DISPLAY)
            .setLore(MSG.LOCATION_UNLOCK_ITEM_LORE)
            .setTouchable(false)
            .build(player, getFinalPlaceholder(player))
    }

    override val minPoint: Point = Point(
            region.minimumPoint.blockX,
            region.minimumPoint.blockY,
            region.minimumPoint.blockZ
        )
    override val maxPoint: Point = Point(
        region.maximumPoint.blockX,
        region.maximumPoint.blockY,
        region.maximumPoint.blockZ
    )

    override fun contains(x: Int, y: Int, z: Int): Boolean {
        return region.contains(com.sk89q.worldedit.Vector(x, y, z))
    }

    val sellZones = HashMap<String, SellZone>()

    fun addSellZone(id: String, msgName: MSGHolder, region: Region, nameHologramVector: Vector) {
        val zone = SellZone(this, id, msgName, region, nameHologramVector)
        sellZones[zone.nameId] = zone
        zone.spawn()
        world.objectsManager.register(zone)
    }

    fun getSellZone(id: String): SellZone? {
        return sellZones[id]
    }

    fun getSellZone(location: Location): SellZone? {
        return world.objectsManager.findSellZones(location).firstOrNull()
    }

    fun getName(player: Player): String {
        return player.getLangMsg(nameMSG).text
    }

    override fun toString(): String {
        return "SimulatorLocation(id='$id', zone=$region, sellZones=$sellZones)"
    }

    override fun beforeBuy(profile: PlayerProfile, silent: Boolean): Boolean {
        if (profile.hasUnlockedLocation(this)) {
            val player = profile.onlinePlayer
            if (player != null && !silent) {
                profile.onlinePlayer?.sendLangMsg(MSG.LOCATION_ALREADY_UNLOCKED, getFinalPlaceholder(player))
            }
            return false
        }
        return true
    }

    override fun afterBuy(profile: PlayerProfile, silent: Boolean) {
        profile.unlockLocation(this)
    }
}