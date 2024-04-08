package org.ndk.godsimulator.newbukkit.util

import org.apache.commons.lang.Validate
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.util.Vector

/**
 * The hit result of a ray trace.
 *
 *
 * Only the hit position is guaranteed to always be available. The availability
 * of the other attributes depends on what got hit and on the context in which
 * the ray trace was performed.
 */
class RayTraceResult private constructor(
    hitPosition: Vector,
    hitBlock: Block?,
    hitBlockFace: BlockFace?,
    hitEntity: Entity?
) {
    private val hitPosition: Vector

    /**
     * Gets the hit block.
     *
     * @return the hit block, or `null` if not available
     */
    val hitBlock: Block?

    /**
     * Gets the hit block face.
     *
     * @return the hit block face, or `null` if not available
     */
    val hitBlockFace: BlockFace?

    /**
     * Gets the hit entity.
     *
     * @return the hit entity, or `null` if not available
     */
    val hitEntity: Entity?

    init {
        Validate.notNull(hitPosition, "Hit position is null!")
        this.hitPosition = hitPosition.clone()
        this.hitBlock = hitBlock
        this.hitBlockFace = hitBlockFace
        this.hitEntity = hitEntity
    }

    /**
     * Creates a RayTraceResult.
     *
     * @param hitPosition the hit position
     */
    constructor(hitPosition: Vector) : this(hitPosition, null, null, null)

    /**
     * Creates a RayTraceResult.
     *
     * @param hitPosition the hit position
     * @param hitBlockFace the hit block face
     */
    constructor(hitPosition: Vector, hitBlockFace: BlockFace?) : this(hitPosition, null, hitBlockFace, null)

    /**
     * Creates a RayTraceResult.
     *
     * @param hitPosition the hit position
     * @param hitBlock the hit block
     * @param hitBlockFace the hit block face
     */
    constructor(hitPosition: Vector, hitBlock: Block?, hitBlockFace: BlockFace?) : this(
        hitPosition,
        hitBlock,
        hitBlockFace,
        null
    )

    /**
     * Creates a RayTraceResult.
     *
     * @param hitPosition the hit position
     * @param hitEntity the hit entity
     */
    constructor(hitPosition: Vector, hitEntity: Entity?) : this(hitPosition, null, null, hitEntity)

    /**
     * Creates a RayTraceResult.
     *
     * @param hitPosition the hit position
     * @param hitEntity the hit entity
     * @param hitBlockFace the hit block face
     */
    constructor(hitPosition: Vector, hitEntity: Entity?, hitBlockFace: BlockFace?) : this(
        hitPosition,
        null,
        hitBlockFace,
        hitEntity
    )

    /**
     * Gets the exact position of the hit.
     *
     * @return a copy of the exact hit position
     */
    fun getHitPosition(): Vector {
        return hitPosition.clone()
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + hitPosition.hashCode()
        result = prime * result + (if ((hitBlock == null)) 0 else hitBlock.hashCode())
        result = prime * result + (if ((hitBlockFace == null)) 0 else hitBlockFace.hashCode())
        result = prime * result + (if ((hitEntity == null)) 0 else hitEntity.hashCode())
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RayTraceResult) return false
        if (hitPosition != other.hitPosition) return false
        if (hitBlock != other.hitBlock) return false
        if (hitBlockFace != other.hitBlockFace) return false
        if (hitEntity != other.hitEntity) return false
        return true
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("RayTraceResult [hitPosition=")
        builder.append(hitPosition)
        builder.append(", hitBlock=")
        builder.append(hitBlock)
        builder.append(", hitBlockFace=")
        builder.append(hitBlockFace)
        builder.append(", hitEntity=")
        builder.append(hitEntity)
        builder.append("]")
        return builder.toString()
    }
}
