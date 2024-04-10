package org.ndk.godsimulator.extension

import com.sk89q.worldedit.Vector2D
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.regions.Polygonal2DRegion
import com.sk89q.worldedit.regions.Region
import com.sk89q.worldedit.regions.factory.SphereRegionFactory
import com.sk89q.worldedit.world.World
import org.bukkit.configuration.ConfigurationSection
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.GodSimulator.Companion.languagesManager
import org.ndk.godsimulator.GodSimulator.Companion.logger
import org.ndk.godsimulator.rpg.buff.EmptyImaginaryBuffsList
import org.ndk.godsimulator.rpg.buff.ImaginaryBuffsList
import org.ndk.godsimulator.rpg.buff.ImaginaryBuffsListImpl
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.language.MSGHolder


val WEVECTOR_ZERO = com.sk89q.worldedit.Vector(0, 0, 0)
fun ConfigurationSection.readWEVector(path: String, def: com.sk89q.worldedit.Vector? = null): com.sk89q.worldedit.Vector? {
    val serialized = getString(path) ?: return def
    try {
        val coords = serialized.substring(1, serialized.length - 1)
            .split(",")
            .map { it.toDouble() }
        return com.sk89q.worldedit.Vector(coords[0], coords[1], coords[2])
    } catch (e: Exception) {
        e.printStackTrace()
        return def
    }
}

fun ConfigurationSection.readRegion(path: String, default: Region? = null): Region? {
    val section = getSection(path) ?: return default
    val type = section.getString("type") ?: return default
    try {
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
            else -> default
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return default
    }
}

fun ConfigurationSection.readRegionOrThrow(path: String): Region {
    return readRegion(path) ?: throwNotFound(path)
}

fun ConfigurationSection.readMSGHolder(path: String, default: MSGHolder? = null): MSGHolder? {
    val msgId = getString(path) ?: return null
    return languagesManager.getMessage(msgId) ?: default
}

fun ConfigurationSection.readMSGHolderOrThrow(path: String): MSGHolder {
    return readMSGHolder(path) ?: throwNotFound(path)
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

    val manager = GodSimulator.rpgManager

    val buffs = ImaginaryBuffsListImpl()
    for ((statName, valueAny) in section.pairs) {
        val value = valueAny.toString()
        val stat = manager.getAnyType(statName)
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


fun ConfigurationSection.readWEVectorOrThrow(path: String): com.sk89q.worldedit.Vector {
    return readWEVector(path) ?: throwNotFound(path)
}

fun ConfigurationSection.readWallet(path: String): Map<String, Long> {
    val section = getSection(path) ?: return emptyMap()
    val wallet = mutableMapOf<String, Long>()
    for ((currency, value) in section.pairs) {
        val long = value as? Long ?: continue
        wallet[currency] = long
    }
    return wallet
}



