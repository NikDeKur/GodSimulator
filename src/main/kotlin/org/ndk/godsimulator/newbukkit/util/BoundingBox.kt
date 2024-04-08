package org.ndk.godsimulator.newbukkit.util

import org.apache.commons.lang.Validate
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs
import org.bukkit.util.NumberConversions
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.math.min

/**
 * A mutable axis aligned bounding box (AABB).
 *
 *
 * This basically represents a rectangular box (specified by minimum and maximum
 * corners) that can for example be used to describe the position and extents of
 * an object (such as an entity, block, or rectangular region) in 3D space. Its
 * edges and faces are parallel to the axes of the cartesian coordinate system.
 *
 *
 * The bounding box may be degenerate (one or more sides having the length 0).
 *
 *
 * Because bounding boxes are mutable, storing them long term may be dangerous
 * if they get modified later. If you want to keep around a bounding box, it may
 * be wise to call [.clone] in order to get a copy.
 */
@SerializableAs("BoundingBox")
class BoundingBox : Cloneable, ConfigurationSerializable {
    /**
     * Gets the minimum x value.
     *
     * @return the minimum x value
     */
    var minX: Double = 0.0
        private set

    /**
     * Gets the minimum y value.
     *
     * @return the minimum y value
     */
    var minY: Double = 0.0
        private set

    /**
     * Gets the minimum z value.
     *
     * @return the minimum z value
     */
    var minZ: Double = 0.0
        private set

    /**
     * Gets the maximum x value.
     *
     * @return the maximum x value
     */
    var maxX: Double = 0.0
        private set

    /**
     * Gets the maximum y value.
     *
     * @return the maximum y value
     */
    var maxY: Double = 0.0
        private set

    /**
     * Gets the maximum z value.
     *
     * @return the maximum z value
     */
    var maxZ: Double = 0.0
        private set

    /**
     * Creates a new (degenerate) bounding box with all corner coordinates at
     * `0`.
     */
    constructor() {
        this.resize(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    }

    /**
     * Creates a new bounding box from the given corner coordinates.
     *
     * @param x1 the first corner's x value
     * @param y1 the first corner's y value
     * @param z1 the first corner's z value
     * @param x2 the second corner's x value
     * @param y2 the second corner's y value
     * @param z2 the second corner's z value
     */
    constructor(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) {
        this.resize(x1, y1, z1, x2, y2, z2)
    }

    /**
     * Resizes this bounding box.
     *
     * @param x1 the first corner's x value
     * @param y1 the first corner's y value
     * @param z1 the first corner's z value
     * @param x2 the second corner's x value
     * @param y2 the second corner's y value
     * @param z2 the second corner's z value
     * @return this bounding box (resized)
     */
    fun resize(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): BoundingBox {
        NumberConversions.checkFinite(x1, "x1 not finite")
        NumberConversions.checkFinite(y1, "y1 not finite")
        NumberConversions.checkFinite(z1, "z1 not finite")
        NumberConversions.checkFinite(x2, "x2 not finite")
        NumberConversions.checkFinite(y2, "y2 not finite")
        NumberConversions.checkFinite(z2, "z2 not finite")

        this.minX = min(x1, x2)
        this.minY = min(y1, y2)
        this.minZ = min(z1, z2)
        this.maxX = max(x1, x2)
        this.maxY = max(y1, y2)
        this.maxZ = max(z1, z2)
        return this
    }

    val min: Vector
        /**
         * Gets the minimum corner as vector.
         *
         * @return the minimum corner as vector
         */
        get() = Vector(minX, minY, minZ)

    val max: Vector
        /**
         * Gets the maximum corner as vector.
         *
         * @return the maximum corner vector
         */
        get() = Vector(maxX, maxY, maxZ)

    val widthX: Double
        /**
         * Gets the width of the bounding box in the x direction.
         *
         * @return the width in the x direction
         */
        get() = (this.maxX - this.minX)

    val widthZ: Double
        /**
         * Gets the width of the bounding box in the z direction.
         *
         * @return the width in the z direction
         */
        get() = (this.maxZ - this.minZ)

    val height: Double
        /**
         * Gets the height of the bounding box.
         *
         * @return the height
         */
        get() = (this.maxY - this.minY)

    val volume: Double
        /**
         * Gets the volume of the bounding box.
         *
         * @return the volume
         */
        get() = (this.height * this.widthX * this.widthZ)

    val centerX: Double
        /**
         * Gets the x coordinate of the center of the bounding box.
         *
         * @return the center's x coordinate
         */
        get() = (this.minX + this.widthX * 0.5)

    val centerY: Double
        /**
         * Gets the y coordinate of the center of the bounding box.
         *
         * @return the center's y coordinate
         */
        get() = (this.minY + this.height * 0.5)

    val centerZ: Double
        /**
         * Gets the z coordinate of the center of the bounding box.
         *
         * @return the center's z coordinate
         */
        get() = (this.minZ + this.widthZ * 0.5)

    val center: Vector
        /**
         * Gets the center of the bounding box.
         *
         * @return the center
         */
        get() = Vector(
            this.centerX,
            centerY,
            centerZ
        )

    /**
     * Copies another bounding box.
     *
     * @param other the other bounding box
     * @return this bounding box
     */
    fun copy(other: BoundingBox): BoundingBox {
        Validate.notNull(other, "Other bounding box is null!")
        return this.resize(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ)
    }

    /**
     * Expands this bounding box by the given values in the corresponding
     * directions.
     *
     *
     * Negative values will shrink the bounding box in the corresponding
     * direction. Shrinking will be limited to the point where the affected
     * opposite faces would meet if the they shrank at uniform speeds.
     *
     * @param negativeX the amount of expansion in the negative x direction
     * @param negativeY the amount of expansion in the negative y direction
     * @param negativeZ the amount of expansion in the negative z direction
     * @param positiveX the amount of expansion in the positive x direction
     * @param positiveY the amount of expansion in the positive y direction
     * @param positiveZ the amount of expansion in the positive z direction
     * @return this bounding box (now expanded)
     */
    fun expand(
        negativeX: Double,
        negativeY: Double,
        negativeZ: Double,
        positiveX: Double,
        positiveY: Double,
        positiveZ: Double
    ): BoundingBox {
        if (negativeX == 0.0 && negativeY == 0.0 && negativeZ == 0.0 && positiveX == 0.0 && positiveY == 0.0 && positiveZ == 0.0) {
            return this
        }
        var newMinX = this.minX - negativeX
        var newMinY = this.minY - negativeY
        var newMinZ = this.minZ - negativeZ
        var newMaxX = this.maxX + positiveX
        var newMaxY = this.maxY + positiveY
        var newMaxZ = this.maxZ + positiveZ

        // limit shrinking:
        if (newMinX > newMaxX) {
            val centerX = this.centerX
            if (newMaxX >= centerX) {
                newMinX = newMaxX
            } else if (newMinX <= centerX) {
                newMaxX = newMinX
            } else {
                newMinX = centerX
                newMaxX = centerX
            }
        }
        if (newMinY > newMaxY) {
            val centerY = this.centerY
            if (newMaxY >= centerY) {
                newMinY = newMaxY
            } else if (newMinY <= centerY) {
                newMaxY = newMinY
            } else {
                newMinY = centerY
                newMaxY = centerY
            }
        }
        if (newMinZ > newMaxZ) {
            val centerZ = this.centerZ
            if (newMaxZ >= centerZ) {
                newMinZ = newMaxZ
            } else if (newMinZ <= centerZ) {
                newMaxZ = newMinZ
            } else {
                newMinZ = centerZ
                newMaxZ = centerZ
            }
        }
        return this.resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ)
    }

    /**
     * Expands this bounding box uniformly by the given values in both positive
     * and negative directions.
     *
     *
     * Negative values will shrink the bounding box. Shrinking will be limited
     * to the bounding box's current size.
     *
     * @param x the amount of expansion in both positive and negative x
     * direction
     * @param y the amount of expansion in both positive and negative y
     * direction
     * @param z the amount of expansion in both positive and negative z
     * direction
     * @return this bounding box (now expanded)
     */
    fun expand(x: Double, y: Double, z: Double): BoundingBox {
        return this.expand(x, y, z, x, y, z)
    }

    /**
     * Expands this bounding box uniformly by the given values in both positive
     * and negative directions.
     *
     *
     * Negative values will shrink the bounding box. Shrinking will be limited
     * to the bounding box's current size.
     *
     * @param expansion the expansion values
     * @return this bounding box (now expanded)
     */
    fun expand(expansion: Vector): BoundingBox {
        Validate.notNull(expansion, "Expansion is null!")
        val x = expansion.x
        val y = expansion.y
        val z = expansion.z
        return this.expand(x, y, z, x, y, z)
    }

    /**
     * Expands this bounding box uniformly by the given value in all directions.
     *
     *
     * A negative value will shrink the bounding box. Shrinking will be limited
     * to the bounding box's current size.
     *
     * @param expansion the amount of expansion
     * @return this bounding box (now expanded)
     */
    fun expand(expansion: Double): BoundingBox {
        return this.expand(expansion, expansion, expansion, expansion, expansion, expansion)
    }

    /**
     * Expands this bounding box in the specified direction.
     *
     *
     * The magnitude of the direction will scale the expansion. A negative
     * expansion value will shrink the bounding box in this direction. Shrinking
     * will be limited to the bounding box's current size.
     *
     * @param dirX the x direction component
     * @param dirY the y direction component
     * @param dirZ the z direction component
     * @param expansion the amount of expansion
     * @return this bounding box (now expanded)
     */
    fun expand(dirX: Double, dirY: Double, dirZ: Double, expansion: Double): BoundingBox {
        if (expansion == 0.0) return this
        if (dirX == 0.0 && dirY == 0.0 && dirZ == 0.0) return this

        val negativeX = (if (dirX < 0.0) (-dirX * expansion) else 0.0)
        val negativeY = (if (dirY < 0.0) (-dirY * expansion) else 0.0)
        val negativeZ = (if (dirZ < 0.0) (-dirZ * expansion) else 0.0)
        val positiveX = (if (dirX > 0.0) (dirX * expansion) else 0.0)
        val positiveY = (if (dirY > 0.0) (dirY * expansion) else 0.0)
        val positiveZ = (if (dirZ > 0.0) (dirZ * expansion) else 0.0)
        return this.expand(negativeX, negativeY, negativeZ, positiveX, positiveY, positiveZ)
    }

    /**
     * Expands this bounding box in the specified direction.
     *
     *
     * The magnitude of the direction will scale the expansion. A negative
     * expansion value will shrink the bounding box in this direction. Shrinking
     * will be limited to the bounding box's current size.
     *
     * @param direction the direction
     * @param expansion the amount of expansion
     * @return this bounding box (now expanded)
     */
    fun expand(direction: Vector, expansion: Double): BoundingBox {
        Validate.notNull(direction, "Direction is null!")
        return this.expand(direction.x, direction.y, direction.z, expansion)
    }

    /**
     * Expands this bounding box in the direction specified by the given block
     * face.
     *
     *
     * A negative expansion value will shrink the bounding box in this
     * direction. Shrinking will be limited to the bounding box's current size.
     *
     * @param blockFace the block face
     * @param expansion the amount of expansion
     * @return this bounding box (now expanded)
     */
    fun expand(blockFace: BlockFace, expansion: Double): BoundingBox {
        Validate.notNull(blockFace, "Block face is null!")
        if (blockFace == BlockFace.SELF) return this

        return this.expand(blockFace.getDirection(), expansion)
    }

    /**
     * Expands this bounding box in the specified direction.
     *
     *
     * Negative values will expand the bounding box in the negative direction,
     * positive values will expand it in the positive direction. The magnitudes
     * of the direction components determine the corresponding amounts of
     * expansion.
     *
     * @param dirX the x direction component
     * @param dirY the y direction component
     * @param dirZ the z direction component
     * @return this bounding box (now expanded)
     */
    fun expandDirectional(dirX: Double, dirY: Double, dirZ: Double): BoundingBox {
        return this.expand(dirX, dirY, dirZ, 1.0)
    }

    /**
     * Expands this bounding box in the specified direction.
     *
     *
     * Negative values will expand the bounding box in the negative direction,
     * positive values will expand it in the positive direction. The magnitude
     * of the direction vector determines the amount of expansion.
     *
     * @param direction the direction and magnitude of the expansion
     * @return this bounding box (now expanded)
     */
    fun expandDirectional(direction: Vector): BoundingBox {
        Validate.notNull(direction, "Expansion is null!")
        return this.expand(direction.x, direction.y, direction.z, 1.0)
    }

    /**
     * Expands this bounding box to contain (or border) the specified position.
     *
     * @param posX the x position value
     * @param posY the y position value
     * @param posZ the z position value
     * @return this bounding box (now expanded)
     * @see .contains
     */
    fun union(posX: Double, posY: Double, posZ: Double): BoundingBox {
        val newMinX = min(this.minX, posX)
        val newMinY = min(this.minY, posY)
        val newMinZ = min(this.minZ, posZ)
        val newMaxX = max(this.maxX, posX)
        val newMaxY = max(this.maxY, posY)
        val newMaxZ = max(this.maxZ, posZ)
        if ((newMinX == this.minX && newMinY == minY && newMinZ == minZ && newMaxX == maxX) && newMaxY == this.maxY && newMaxZ == this.maxZ) {
            return this
        }
        return this.resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ)
    }

    /**
     * Expands this bounding box to contain (or border) the specified position.
     *
     * @param position the position
     * @return this bounding box (now expanded)
     * @see .contains
     */
    fun union(position: Vector): BoundingBox {
        Validate.notNull(position, "Position is null!")
        return this.union(position.x, position.y, position.z)
    }

    /**
     * Expands this bounding box to contain (or border) the specified position.
     *
     * @param position the position
     * @return this bounding box (now expanded)
     * @see .contains
     */
    fun union(position: Location): BoundingBox {
        Validate.notNull(position, "Position is null!")
        return this.union(position.x, position.y, position.z)
    }

    /**
     * Expands this bounding box to contain both this and the given bounding
     * box.
     *
     * @param other the other bounding box
     * @return this bounding box (now expanded)
     */
    fun union(other: BoundingBox): BoundingBox {
        Validate.notNull(other, "Other bounding box is null!")
        if (this.contains(other)) return this
        val newMinX = min(this.minX, other.minX)
        val newMinY = min(this.minY, other.minY)
        val newMinZ = min(this.minZ, other.minZ)
        val newMaxX = max(this.maxX, other.maxX)
        val newMaxY = max(this.maxY, other.maxY)
        val newMaxZ = max(this.maxZ, other.maxZ)
        return this.resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ)
    }

    /**
     * Resizes this bounding box to represent the intersection of this and the
     * given bounding box.
     *
     * @param other the other bounding box
     * @return this bounding box (now representing the intersection)
     * @throws IllegalArgumentException if the bounding boxes don't overlap
     */
    fun intersection(other: BoundingBox): BoundingBox {
        Validate.notNull(other, "Other bounding box is null!")
        Validate.isTrue(this.overlaps(other), "The bounding boxes do not overlap!")
        val newMinX = max(this.minX, other.minX)
        val newMinY = max(this.minY, other.minY)
        val newMinZ = max(this.minZ, other.minZ)
        val newMaxX = min(this.maxX, other.maxX)
        val newMaxY = min(this.maxY, other.maxY)
        val newMaxZ = min(this.maxZ, other.maxZ)
        return this.resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ)
    }

    /**
     * Shifts this bounding box by the given amounts.
     *
     * @param shiftX the shift in x direction
     * @param shiftY the shift in y direction
     * @param shiftZ the shift in z direction
     * @return this bounding box (now shifted)
     */
    fun shift(shiftX: Double, shiftY: Double, shiftZ: Double): BoundingBox {
        if (shiftX == 0.0 && shiftY == 0.0 && shiftZ == 0.0) return this
        return this.resize(
            this.minX + shiftX, this.minY + shiftY, this.minZ + shiftZ,
            this.maxX + shiftX, this.maxY + shiftY, this.maxZ + shiftZ
        )
    }

    /**
     * Shifts this bounding box by the given amounts.
     *
     * @param shift the shift
     * @return this bounding box (now shifted)
     */
    fun shift(shift: Vector): BoundingBox {
        Validate.notNull(shift, "Shift is null!")
        return this.shift(shift.x, shift.y, shift.z)
    }

    /**
     * Shifts this bounding box by the given amounts.
     *
     * @param shift the shift
     * @return this bounding box (now shifted)
     */
    fun shift(shift: Location): BoundingBox {
        Validate.notNull(shift, "Shift is null!")
        return this.shift(shift.x, shift.y, shift.z)
    }

    private fun overlaps(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): Boolean {
        return this.minX < maxX && (this.maxX > minX
                ) && (this.minY < maxY) && (this.maxY > minY
                ) && (this.minZ < maxZ) && (this.maxZ > minZ)
    }

    /**
     * Checks if this bounding box overlaps with the given bounding box.
     *
     *
     * Bounding boxes that are only intersecting at the borders are not
     * considered overlapping.
     *
     * @param other the other bounding box
     * @return `true` if overlapping
     */
    fun overlaps(other: BoundingBox): Boolean {
        Validate.notNull(other, "Other bounding box is null!")
        return this.overlaps(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ)
    }

    /**
     * Checks if this bounding box overlaps with the bounding box that is
     * defined by the given corners.
     *
     *
     * Bounding boxes that are only intersecting at the borders are not
     * considered overlapping.
     *
     * @param min the first corner
     * @param max the second corner
     * @return `true` if overlapping
     */
    fun overlaps(min: Vector, max: Vector): Boolean {
        Validate.notNull(min, "Min is null!")
        Validate.notNull(max, "Max is null!")
        val x1 = min.x
        val y1 = min.y
        val z1 = min.z
        val x2 = max.x
        val y2 = max.y
        val z2 = max.z
        return this.overlaps(
            min(x1, x2), min(y1, y2), min(z1, z2),
            max(x1, x2), max(y1, y2), max(z1, z2)
        )
    }

    /**
     * Checks if this bounding box contains the specified position.
     *
     *
     * Positions exactly on the minimum borders of the bounding box are
     * considered to be inside the bounding box, while positions exactly on the
     * maximum borders are considered to be outside. This allows bounding boxes
     * to reside directly next to each other with positions always only residing
     * in exactly one of them.
     *
     * @param x the position's x coordinates
     * @param y the position's y coordinates
     * @param z the position's z coordinates
     * @return `true` if the bounding box contains the position
     */
    fun contains(x: Double, y: Double, z: Double): Boolean {
        return (x >= this.minX && x < maxX && y >= minY && y < maxY) && z >= this.minZ && z < this.maxZ
    }

    /**
     * Checks if this bounding box contains the specified position.
     *
     *
     * Positions exactly on the minimum borders of the bounding box are
     * considered to be inside the bounding box, while positions exactly on the
     * maximum borders are considered to be outside. This allows bounding boxes
     * to reside directly next to each other with positions always only residing
     * in exactly one of them.
     *
     * @param position the position
     * @return `true` if the bounding box contains the position
     */
    fun contains(position: Vector): Boolean {
        Validate.notNull(position, "Position is null!")
        return this.contains(position.x, position.y, position.z)
    }

    private fun contains(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): Boolean {
        return this.minX <= minX && (this.maxX >= maxX
                ) && (this.minY <= minY) && (this.maxY >= maxY
                ) && (this.minZ <= minZ) && (this.maxZ >= maxZ)
    }

    /**
     * Checks if this bounding box fully contains the given bounding box.
     *
     * @param other the other bounding box
     * @return `true` if the bounding box contains the given bounding
     * box
     */
    fun contains(other: BoundingBox): Boolean {
        Validate.notNull(other, "Other bounding box is null!")
        return this.contains(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ)
    }

    /**
     * Checks if this bounding box fully contains the bounding box that is
     * defined by the given corners.
     *
     * @param min the first corner
     * @param max the second corner
     * @return `true` if the bounding box contains the specified
     * bounding box
     */
    fun contains(min: Vector, max: Vector): Boolean {
        Validate.notNull(min, "Min is null!")
        Validate.notNull(max, "Max is null!")
        val x1 = min.x
        val y1 = min.y
        val z1 = min.z
        val x2 = max.x
        val y2 = max.y
        val z2 = max.z
        return this.contains(
            min(x1, x2), min(y1, y2), min(z1, z2),
            max(x1, x2), max(y1, y2), max(z1, z2)
        )
    }

    /**
     * Calculates the intersection of this bounding box with the specified line
     * segment.
     *
     *
     * Intersections at edges and corners yield one of the affected block faces
     * as hit result, but it is not defined which of them.
     *
     * @param start the start position
     * @param direction the ray direction
     * @param maxDistance the maximum distance
     * @return the ray trace hit result, or `null` if there is no hit
     */
    fun rayTrace(start: Vector, direction: Vector, maxDistance: Double): RayTraceResult? {
        Validate.notNull(start, "Start is null!")
        start.checkFinite()
        Validate.notNull(direction, "Direction is null!")
        direction.checkFinite()
        Validate.isTrue(direction.lengthSquared() > 0, "Direction's magnitude is 0!")
        if (maxDistance < 0.0) return null

        // ray start:
        val startX = start.x
        val startY = start.y
        val startZ = start.z

        // ray direction:
        val dir: Vector = direction.clone().normalizeZeros().normalize()
        val dirX = dir.x
        val dirY = dir.y
        val dirZ = dir.z

        // saving a few divisions below:
        // Note: If one of the direction vector components is 0.0, these
        // divisions result in infinity. But this is not a problem.
        val divX = 1.0 / dirX
        val divY = 1.0 / dirY
        val divZ = 1.0 / dirZ

        var tMin: Double
        var tMax: Double
        var hitBlockFaceMin: BlockFace?
        var hitBlockFaceMax: BlockFace?

        // intersections with x planes:
        if (dirX >= 0.0) {
            tMin = (this.minX - startX) * divX
            tMax = (this.maxX - startX) * divX
            hitBlockFaceMin = BlockFace.WEST
            hitBlockFaceMax = BlockFace.EAST
        } else {
            tMin = (this.maxX - startX) * divX
            tMax = (this.minX - startX) * divX
            hitBlockFaceMin = BlockFace.EAST
            hitBlockFaceMax = BlockFace.WEST
        }

        // intersections with y planes:
        val tyMin: Double
        val tyMax: Double
        val hitBlockFaceYMin: BlockFace
        val hitBlockFaceYMax: BlockFace
        if (dirY >= 0.0) {
            tyMin = (this.minY - startY) * divY
            tyMax = (this.maxY - startY) * divY
            hitBlockFaceYMin = BlockFace.DOWN
            hitBlockFaceYMax = BlockFace.UP
        } else {
            tyMin = (this.maxY - startY) * divY
            tyMax = (this.minY - startY) * divY
            hitBlockFaceYMin = BlockFace.UP
            hitBlockFaceYMax = BlockFace.DOWN
        }
        if ((tMin > tyMax) || (tMax < tyMin)) {
            return null
        }
        if (tyMin > tMin) {
            tMin = tyMin
            hitBlockFaceMin = hitBlockFaceYMin
        }
        if (tyMax < tMax) {
            tMax = tyMax
            hitBlockFaceMax = hitBlockFaceYMax
        }

        // intersections with z planes:
        val tzMin: Double
        val tzMax: Double
        val hitBlockFaceZMin: BlockFace
        val hitBlockFaceZMax: BlockFace
        if (dirZ >= 0.0) {
            tzMin = (this.minZ - startZ) * divZ
            tzMax = (this.maxZ - startZ) * divZ
            hitBlockFaceZMin = BlockFace.NORTH
            hitBlockFaceZMax = BlockFace.SOUTH
        } else {
            tzMin = (this.maxZ - startZ) * divZ
            tzMax = (this.minZ - startZ) * divZ
            hitBlockFaceZMin = BlockFace.SOUTH
            hitBlockFaceZMax = BlockFace.NORTH
        }
        if ((tMin > tzMax) || (tMax < tzMin)) {
            return null
        }
        if (tzMin > tMin) {
            tMin = tzMin
            hitBlockFaceMin = hitBlockFaceZMin
        }
        if (tzMax < tMax) {
            tMax = tzMax
            hitBlockFaceMax = hitBlockFaceZMax
        }

        // intersections are behind the start:
        if (tMax < 0.0) return null
        // intersections are to far away:
        if (tMin > maxDistance) {
            return null
        }

        // find the closest intersection:
        val t: Double
        val hitBlockFace: BlockFace
        if (tMin < 0.0) {
            t = tMax
            hitBlockFace = hitBlockFaceMax
        } else {
            t = tMin
            hitBlockFace = hitBlockFaceMin
        }
        // reusing the newly created direction vector for the hit position:
        val hitPosition = dir.multiply(t).add(start)
        return RayTraceResult(hitPosition, hitBlockFace)
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        var temp = java.lang.Double.doubleToLongBits(maxX)
        result = prime * result + (temp xor (temp ushr 32)).toInt()
        temp = java.lang.Double.doubleToLongBits(maxY)
        result = prime * result + (temp xor (temp ushr 32)).toInt()
        temp = java.lang.Double.doubleToLongBits(maxZ)
        result = prime * result + (temp xor (temp ushr 32)).toInt()
        temp = java.lang.Double.doubleToLongBits(minX)
        result = prime * result + (temp xor (temp ushr 32)).toInt()
        temp = java.lang.Double.doubleToLongBits(minY)
        result = prime * result + (temp xor (temp ushr 32)).toInt()
        temp = java.lang.Double.doubleToLongBits(minZ)
        result = prime * result + (temp xor (temp ushr 32)).toInt()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj !is BoundingBox) return false
        val other = obj
        if (java.lang.Double.doubleToLongBits(maxX) != java.lang.Double.doubleToLongBits(other.maxX)) return false
        if (java.lang.Double.doubleToLongBits(maxY) != java.lang.Double.doubleToLongBits(other.maxY)) return false
        if (java.lang.Double.doubleToLongBits(maxZ) != java.lang.Double.doubleToLongBits(other.maxZ)) return false
        if (java.lang.Double.doubleToLongBits(minX) != java.lang.Double.doubleToLongBits(other.minX)) return false
        if (java.lang.Double.doubleToLongBits(minY) != java.lang.Double.doubleToLongBits(other.minY)) return false
        if (java.lang.Double.doubleToLongBits(minZ) != java.lang.Double.doubleToLongBits(other.minZ)) return false
        return true
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("BoundingBox [minX=")
        builder.append(minX)
        builder.append(", minY=")
        builder.append(minY)
        builder.append(", minZ=")
        builder.append(minZ)
        builder.append(", maxX=")
        builder.append(maxX)
        builder.append(", maxY=")
        builder.append(maxY)
        builder.append(", maxZ=")
        builder.append(maxZ)
        builder.append("]")
        return builder.toString()
    }

    /**
     * Creates a copy of this bounding box.
     *
     * @return the cloned bounding box
     */
    public override fun clone(): BoundingBox {
        try {
            return super.clone() as BoundingBox
        } catch (e: CloneNotSupportedException) {
            throw Error(e)
        }
    }

    override fun serialize(): Map<String, Any> {
        val result: MutableMap<String, Any> = LinkedHashMap()
        result["minX"] = minX
        result["minY"] = minY
        result["minZ"] = minZ
        result["maxX"] = maxX
        result["maxY"] = maxY
        result["maxZ"] = maxZ
        return result
    }

    companion object {
        /**
         * Creates a new bounding box using the coordinates of the given vectors as
         * corners.
         *
         * @param corner1 the first corner
         * @param corner2 the second corner
         * @return the bounding box
         */
        fun of(corner1: Vector, corner2: Vector): BoundingBox {
            Validate.notNull(corner1, "Corner1 is null!")
            Validate.notNull(corner2, "Corner2 is null!")
            return BoundingBox(corner1.x, corner1.y, corner1.z, corner2.x, corner2.y, corner2.z)
        }

        /**
         * Creates a new bounding box using the coordinates of the given locations
         * as corners.
         *
         * @param corner1 the first corner
         * @param corner2 the second corner
         * @return the bounding box
         */
        fun of(corner1: Location, corner2: Location): BoundingBox {
            Validate.notNull(corner1, "Corner1 is null!")
            Validate.notNull(corner2, "Corner2 is null!")
            Validate.isTrue(corner1.world == corner2.world, "Locations from different worlds!")
            return BoundingBox(corner1.x, corner1.y, corner1.z, corner2.x, corner2.y, corner2.z)
        }

        /**
         * Creates a new bounding box using the coordinates of the given blocks as
         * corners.
         *
         *
         * The bounding box will be sized to fully contain both blocks.
         *
         * @param corner1 the first corner block
         * @param corner2 the second corner block
         * @return the bounding box
         */
        fun of(corner1: Block, corner2: Block): BoundingBox {
            Validate.notNull(corner1, "Corner1 is null!")
            Validate.notNull(corner2, "Corner2 is null!")
            Validate.isTrue(corner1.world == corner2.world, "Blocks from different worlds!")

            val x1 = corner1.x
            val y1 = corner1.y
            val z1 = corner1.z
            val x2 = corner2.x
            val y2 = corner2.y
            val z2 = corner2.z

            val minX = min(x1.toDouble(), x2.toDouble()).toInt()
            val minY = min(y1.toDouble(), y2.toDouble()).toInt()
            val minZ = min(z1.toDouble(), z2.toDouble()).toInt()
            val maxX = (max(x1.toDouble(), x2.toDouble()) + 1).toInt()
            val maxY = (max(y1.toDouble(), y2.toDouble()) + 1).toInt()
            val maxZ = (max(z1.toDouble(), z2.toDouble()) + 1).toInt()

            return BoundingBox(
                minX.toDouble(),
                minY.toDouble(),
                minZ.toDouble(),
                maxX.toDouble(),
                maxY.toDouble(),
                maxZ.toDouble()
            )
        }

        /**
         * Creates a new 1x1x1 sized bounding box containing the given block.
         *
         * @param block the block
         * @return the bounding box
         */
        fun of(block: Block): BoundingBox {
            Validate.notNull(block, "Block is null!")
            return BoundingBox(
                block.x.toDouble(),
                block.y.toDouble(),
                block.z.toDouble(),
                (block.x + 1).toDouble(),
                (block.y + 1).toDouble(),
                (block.z + 1).toDouble()
            )
        }

        /**
         * Creates a new bounding box using the given center and extents.
         *
         * @param center the center
         * @param x 1/2 the size of the bounding box along the x axis
         * @param y 1/2 the size of the bounding box along the y axis
         * @param z 1/2 the size of the bounding box along the z axis
         * @return the bounding box
         */
        fun of(center: Vector, x: Double, y: Double, z: Double): BoundingBox {
            Validate.notNull(center, "Center is null!")
            return BoundingBox(center.x - x, center.y - y, center.z - z, center.x + x, center.y + y, center.z + z)
        }

        /**
         * Creates a new bounding box using the given center and extents.
         *
         * @param center the center
         * @param x 1/2 the size of the bounding box along the x axis
         * @param y 1/2 the size of the bounding box along the y axis
         * @param z 1/2 the size of the bounding box along the z axis
         * @return the bounding box
         */
        fun of(center: Location, x: Double, y: Double, z: Double): BoundingBox {
            Validate.notNull(center, "Center is null!")
            return BoundingBox(center.x - x, center.y - y, center.z - z, center.x + x, center.y + y, center.z + z)
        }

        fun deserialize(args: Map<String?, Any?>): BoundingBox {
            var minX = 0.0
            var minY = 0.0
            var minZ = 0.0
            var maxX = 0.0
            var maxY = 0.0
            var maxZ = 0.0

            if (args.containsKey("minX")) {
                minX = (args["minX"] as Number?)!!.toDouble()
            }
            if (args.containsKey("minY")) {
                minY = (args["minY"] as Number?)!!.toDouble()
            }
            if (args.containsKey("minZ")) {
                minZ = (args["minZ"] as Number?)!!.toDouble()
            }
            if (args.containsKey("maxX")) {
                maxX = (args["maxX"] as Number?)!!.toDouble()
            }
            if (args.containsKey("maxY")) {
                maxY = (args["maxY"] as Number?)!!.toDouble()
            }
            if (args.containsKey("maxZ")) {
                maxZ = (args["maxZ"] as Number?)!!.toDouble()
            }

            return BoundingBox(minX, minY, minZ, maxX, maxY, maxZ)
        }


        fun BlockFace.getDirection(): Vector {
            val direction: Vector = Vector(modX, modY, modZ)
            if (modX != 0 || modY != 0 || modZ != 0) {
                direction.normalize()
            }
            return direction
        }

        fun Vector.normalizeZeros(): Vector {
            if (x == -0.0) x = 0.0
            if (y == -0.0) y = 0.0
            if (z == -0.0) z = 0.0
            return this
        }
    }

}
