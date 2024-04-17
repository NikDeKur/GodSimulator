package org.ndk.godsimulator.rpg.regen

import org.ndk.global.interfaces.Snowflake
import org.ndk.godsimulator.rpg.profile.RPGPlayerProfile

interface Regeneration : Snowflake<String> {

    /**
     * The delay in ticks between each regeneration.
     */
    val delay: Long

    /**
     * Regenerates the profile.
     *
     * @param rpg profile to regenerate.
     */
    fun regenerate(rpg: RPGPlayerProfile)
}