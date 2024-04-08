@file:Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")

package org.ndk.godsimulator.selling

import com.sk89q.worldedit.regions.Region
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.ndk.global.spatial.Point
import org.ndk.godsimulator.extension.toLocation
import org.ndk.godsimulator.extension.toWEVector
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.godsimulator.location.SimulatorLocation
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.wobject.Object
import org.ndk.klib.format
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.language.MSGHolder
import java.math.BigInteger

class SellZone(
    val simulatorLocation: SimulatorLocation,
    val nameId: String,
    override val nameMSG: MSGHolder,
    val region: Region,
    override val hologramSpawnTranslation: Vector,
    val multiplier: Double
) : Object(), Region by region, MSGNameHolder {


    override val defaultPhName: String = "zone"
    override val placeholderMap: MutableMap<String, Any>
        get() = super<Object>.placeholderMap.also {
            var multiplier = this.multiplier.format(1)
            if (!multiplier.contains(".")) {
                multiplier += ".0"
            }
            it["multiplier"] = multiplier
        }
    override val location: Location
        get() = minimumPoint.toLocation(simulatorLocation.world.bukkit)

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

    override inline fun contains(x: Int, y: Int, z: Int): Boolean {
        return region.contains(com.sk89q.worldedit.Vector(x, y, z))
    }

    override fun getHologramText(player: Player): Collection<String> {
        return player.getLangMsg(MSG.SELL_ZONE_HOLOGRAM_TEXT, getFinalPlaceholder(player)).listText
    }


    val multiplierBig = multiplier.toBigDecimal()
    fun isInside(player: Player) = contains(player.location.toWEVector())

    fun getSellPrice(amount: BigInteger): BigInteger {
        return amount.toBigDecimal().times(multiplierBig).toBigInteger()
    }

    fun sell(profile: PlayerProfile): BigInteger {
        val price = getSellPrice(profile.bagFill)
        profile.clearBag()
        profile.wallet.giveCoins(price)
        return price
    }




    override fun toString(): String {
        return "SellZone(id='$nameId', region=$region, multiplier=$multiplier)"
    }
}