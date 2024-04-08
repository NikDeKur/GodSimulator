@file:Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")

package org.ndk.godsimulator.wobject.building

import com.sk89q.worldedit.regions.Region
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.ndk.global.spatial.Point
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.extension.toWEVector
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.reward.BagFillReward
import org.ndk.godsimulator.reward.ExperienceReward
import org.ndk.godsimulator.wobject.LootableLivingObject
import org.ndk.minecraft.extension.fillArea
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.language.MSGHolder
import java.math.BigInteger

class Building(
    val pattern: BuildingPattern,
    override val respawnTicks: Long,
    override val location: Location,
    val level: Int,
    override val maxHealth: BigInteger,
    val expDrop: BigInteger,
    val bagFill: BigInteger,
) : LootableLivingObject() {

    init {
        reward.addReward(ExperienceReward(expDrop))
        reward.addReward(BagFillReward(bagFill))
    }

    override val defaultPhName: String = "building"
    override val nameMSG: MSGHolder = pattern.nameMSG

    val buildingsManager = pattern.buildingsManager
    lateinit var region: Region



    override val hologramSpawnTranslation: Vector = pattern.hologramTranslation

    override fun getHologramText(player: Player): Collection<String> {
        return player.getLangMsg(MSG.BUILDING_HOLOGRAM_TEXT, getFinalPlaceholder(player)).listText
    }


    init {
        try {
            spawn()
        } catch (e: NoSuchFileException) {
            GodSimulator.logger.warning("Error while spawning building. Schematic '${pattern.schemName}'! File not found!")
        }
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

    override inline fun contains(x: Int, y: Int, z: Int) = region.contains(com.sk89q.worldedit.Vector(x, y, z))

    override fun spawn() {
        region = buildingsManager.pasteBuilding(location, pattern.schem)
        super.spawn()
    }

    override fun remove() {
        val x1 = region.minimumPoint.blockX
        val y1 = region.minimumPoint.blockY
        val z1 = region.minimumPoint.blockZ

        val x2 = region.maximumPoint.blockX
        val y2 = region.maximumPoint.blockY
        val z2 = region.maximumPoint.blockZ
        location.world.fillArea(Material.AIR, x1, y1, z1, x2, y2, z2)

        super.remove()
    }


    fun contains(location: Location): Boolean {
        return region.contains(location.toWEVector())
    }

    override fun toString(): String {
        val loc = "(${location.blockX}, ${location.blockY}, ${location.blockZ})"
        return "Building(id=$id, location=$loc, pattern=$pattern, level=$level, health=$health/$maxHealth)"
    }
}


