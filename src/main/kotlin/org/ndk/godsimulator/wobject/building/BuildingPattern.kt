package org.ndk.godsimulator.wobject.building

import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.regions.Region
import org.bukkit.util.Vector
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.minecraft.language.MSGHolder

class BuildingPattern(
    val buildingsManager: BuildingsManager,
    val id: String,
    override val nameMSG: MSGHolder,
    val schemName: String,
    val hologramTranslation: Vector,
) : MSGNameHolder {

    val schem: Clipboard = loadSchem()

    val region: Region
        get() = schem.region

    override val defaultPhName: String = "pattern"


    fun contains(vector: Vector) = region.contains(com.sk89q.worldedit.Vector(vector.x, vector.y, vector.z))


    fun loadSchem(): Clipboard {
        return buildingsManager.loadSchematic(schemName)
    }

    override fun toString(): String {
        return "BuildingPattern(id='$id', schemName='$schemName')"
    }
}