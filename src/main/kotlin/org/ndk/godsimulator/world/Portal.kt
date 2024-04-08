package org.ndk.godsimulator.world


import com.sk89q.worldedit.Vector
import com.sk89q.worldedit.regions.Region
import org.bukkit.Location
import org.bukkit.entity.Player
import org.ndk.global.spatial.Point
import org.ndk.godsimulator.extension.toLocation
import org.ndk.godsimulator.extension.toPoint
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.godsimulator.wobject.Object
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.extension.scanAllPlayers
import org.ndk.minecraft.extension.tracker
import org.ndk.minecraft.language.MSGHolder

data class Portal(
    val idStr: String,
    override val nameMSG: MSGHolder,
    override val hologramSpawnTranslation: org.bukkit.util.Vector,
    val region: Region,
    val destination: Location,
) : Object(), MSGNameHolder {

    override val defaultPhName: String = "portal"

    fun teleport(player: Player) {
        player.teleport(destination)
        for (entity in player.world.entities) {
            entity.tracker.scanAllPlayers()
        }
    }

    override val location: Location = region.minimumPoint.toLocation(destination.world)

    override fun getHologramText(player: Player): Collection<String> {
        return player.getLangMsg(nameMSG, getFinalPlaceholder(player)).listText
    }

    override val minPoint: Point = region.minimumPoint.toPoint()
    override val maxPoint: Point = region.maximumPoint.toPoint()

    override fun contains(x: Int, y: Int, z: Int): Boolean {
        return region.contains(Vector(x, y, z))
    }
}