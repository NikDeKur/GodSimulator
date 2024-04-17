package org.ndk.godsimulator.extension

import com.sk89q.worldedit.Vector
import com.sk89q.worldedit.Vector2D
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.regions.Polygonal2DRegion
import com.sk89q.worldedit.regions.Region
import com.sk89q.worldedit.regions.factory.SphereRegionFactory
import com.sk89q.worldedit.world.World
import org.bukkit.configuration.ConfigurationSection
import org.ndk.godsimulator.GodSimulator.Companion.logger
import org.ndk.godsimulator.economy.currency.CurrencyManager
import org.ndk.godsimulator.economy.wallet.Wallet
import org.ndk.godsimulator.economy.wallet.WalletImpl
import org.ndk.godsimulator.equipable.Rarity
import org.ndk.godsimulator.rpg.RPGManager
import org.ndk.godsimulator.rpg.buff.EmptyImaginaryBuffsList
import org.ndk.godsimulator.rpg.buff.ImaginaryBuffsList
import org.ndk.godsimulator.rpg.buff.ImaginaryBuffsListImpl
import org.ndk.klib.enumValueOfOrNull
import org.ndk.minecraft.extension.*


val WEVECTOR_ZERO = Vector(0, 0, 0)

fun ConfigurationSection.readWEVectorOrThrow(path: String): Vector {
    val serialized = getString(path) ?: throwNotFound(path)
    val coords = try {
        serialized.substring(1, serialized.length - 1)
            .split(",")
            .map { it.toDouble() }
    } catch (e: NumberFormatException) {
        throwReport(path, "Invalid number format: $serialized")
    }
    return Vector(coords[0], coords[1], coords[2])
}

fun ConfigurationSection.readWEVector(path: String, def: Vector? = null): Vector? {
    return try {
        readWEVectorOrThrow(path)
    } catch (e: Exception) {
        return def
    }
}

fun ConfigurationSection.readRegionOrThrow(path: String): Region {
    val section = getSectionOrThrow(path)
    val type = section.getStringOrThrow("type")
    return when (type) {
        "cuboid" -> {
            val min = section.readWEVectorOrThrow("min")
            val max = section.readWEVectorOrThrow("max")
            CuboidRegion(min, max)
        }

        "circle" -> {
            val base = section.readWEVectorOrThrow("center")
            val radius = section.getIntOrThrow("radius")

            val region = com.sk89q.worldedit.regions.CylinderRegion()
            region.setCenter(Vector2D(base.x, base.z))
            region.minimumY = base.y.toInt()
            region.maximumY = base.y.toInt()
            region.radius = Vector2D(radius, radius)
            region
        }

        "cylinder" -> {
            val center = section.readWEVectorOrThrow("center")
            val radius = section.getIntOrThrow("radius")
            val height = section.getIntOrThrow("height")

            val region = com.sk89q.worldedit.regions.CylinderRegion(
                null as World?,
                center,
                Vector2D(radius, radius),
                center.y.toInt(),
                center.y.toInt() + height
            )
            region
        }

        "sphere" -> {
            val center = section.readWEVectorOrThrow("center")
            val radius = section.getDoubleOrThrow("radius")

            val region = SphereRegionFactory().createCenteredAt(center, radius)
            region
        }

        "polygonal" -> {
            val points = section.getListSection("points")
                .map { it.readWEVectorOrThrow("point") }
            val region = Polygonal2DRegion()
            points.forEach(region::addPoint)
            region
        }

        "convex" -> {
            val points = section.getListSection("points")
                .map { it.readWEVectorOrThrow("point") }
            val region = com.sk89q.worldedit.regions.ConvexPolyhedralRegion(null as World?)
            points.forEach(region::addVertex)
            region
        }
        else -> throwReport(path, "Unknown region type: $type")
    }
}

fun ConfigurationSection.readRegion(path: String, default: Region? = null): Region? {
    return try {
        readRegionOrThrow(path)
    } catch (e: Exception) {
        default
    }
}


/**
 * Reads imaginary buffs from the section at the specified path.
 *
 * Returns [EmptyImaginaryBuffsList] if the section does not exist or buffs are not found.
 *
 * Reports errors to the console if the stat name is unknown or the value is invalid.
 *
 * @param path Path to the section with buffs.
 * @return List of imaginary buffs.
 * @see ImaginaryBuffsList
 */
fun ConfigurationSection.readStats(path: String): ImaginaryBuffsList {
    val section = getSection(path) ?: return EmptyImaginaryBuffsList

    val buffs = ImaginaryBuffsListImpl()
    for ((statName, valueAny) in section.pairs) {
        val value = valueAny.toString()
        val stat = RPGManager.getAnyType(statName)
        if (stat == null) {
            logger.warning("While reading stats at path '${this.currentPath}': Unknown stat name: '$statName'")
            continue
        }
        val statValue = stat.read(value)
        if (statValue == null) {
            logger.warning("While reading stats at path '${this.currentPath}': Failed to read value '$value' for stat '$statName")
            continue
        }

        buffs.addBuff(stat, statValue)
    }

    return buffs.ifEmpty { EmptyImaginaryBuffsList }
}




fun ConfigurationSection.readWallet(path: String): WalletImpl {
    val section = getSection(path) ?: return Wallet.EMPTY
    val wallet = WalletImpl()
    for ((currencyStr, value) in section.pairs) {
        val currency = CurrencyManager.getCurrency(currencyStr) ?: throw IllegalArgumentException("Currency $currencyStr not found")
        val long = (value as? Number)?.toDouble()?.toBigDecimal()?.toBigInteger() ?: throw IllegalArgumentException("Invalid value for currency $currencyStr")
        wallet[currency] = long
    }
    return wallet
}

fun ConfigurationSection.readRarityOrThrow(path: String): Rarity {
    val rarity = getString(path) ?: throwNotFound(path)
    return enumValueOfOrNull<Rarity>(rarity.uppercase()) ?: throwReport(path, "unknown rarity: $rarity")
}

fun ConfigurationSection.readRarity(path: String, default: Rarity? = null): Rarity? {
    return try {
        readRarityOrThrow(path)
    } catch (e: Exception) {
        default
    }
}

