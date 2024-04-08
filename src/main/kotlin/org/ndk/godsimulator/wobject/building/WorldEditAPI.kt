package org.ndk.godsimulator.wobject.building

import com.sk89q.worldedit.EditSession
import com.sk89q.worldedit.Vector
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.blocks.BaseBlock
import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard
import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat
import com.sk89q.worldedit.function.operation.ForwardExtentCopy
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.math.transform.AffineTransform
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.regions.Region
import com.sk89q.worldedit.session.ClipboardHolder
import com.sk89q.worldedit.util.io.Closer
import com.sk89q.worldedit.world.World
import org.bukkit.Location
import org.ndk.godsimulator.extension.toWEVector
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object WorldEditAPI {



    fun newSession(world: org.bukkit.World): EditSession {
        return newSession(BukkitWorld(world))
    }

    fun newSession(world: World): EditSession {
        return WorldEdit.getInstance().editSessionFactory.getEditSession(world, -1)
    }


    fun loadSchematic(file: File): Clipboard {
        val format = ClipboardFormat.findByFile(file) ?: throw NoSuchFileException(file)
        return format.getReader(FileInputStream(file)).read(null)
    }


    fun pasteSchematic(location: Location, file: File, side: WorldSide = WorldSide.EAST): Region {
        return pasteSchematic(location, loadSchematic(file), side)
    }

    /**
     * @param location - location where schematic will be pasted (Facing to EAST)
     */
    fun pasteSchematic(location: Location, schematic: Clipboard, side: WorldSide = WorldSide.EAST): Region {
        val world = BukkitWorld(location.world) as World
        val region = schematic.region
        val edit = newSession(world)
        val operation = ClipboardHolder(schematic, world.worldData)
            .createPaste(edit, world.worldData)
            .to(Vector(location.x, location.y, location.z))
            .ignoreAirBlocks(false)
            .build() as ForwardExtentCopy

        operation.transform = operation.transform.combine(AffineTransform().rotateY(-(side.angle.toDouble())))

        Operations.complete(operation)

        val clipboardOffset: Vector = schematic.region.minimumPoint.subtract(schematic.origin)
        val realTo: Vector = location.toWEVector().add(operation.transform.apply(clipboardOffset))
        val max = realTo.add(operation.transform.apply(region.maximumPoint.subtract(region.minimumPoint)))
        return CuboidRegion(realTo, max)
    }


    fun saveSchematic(region: Region, origin: Vector, file: File) {
        val world = region.world!!
        val edit = newSession(world)

        val clipboard = BlockArrayClipboard(region)
        clipboard.origin = origin
        val copy = ForwardExtentCopy(edit, region, clipboard, region.minimumPoint)

        Operations.complete(copy)

        Closer.create().use {
            val output = it.register(FileOutputStream(file))
            val writer = it.register(ClipboardFormat.SCHEMATIC.getWriter(output))
            writer.write(clipboard, world.worldData)
        }
    }

    fun clear(region: Region): Int {
        val world = region.world!!
        val edit = newSession(world)
        return edit.setBlocks(region, BaseBlock(0))
    }

    enum class WorldSide(val angle: Int) {
        EAST(0),
        SOUTH(90),
        WEST(180),
        NORTH(270),
    }
}