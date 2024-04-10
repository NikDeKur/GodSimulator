package org.ndk.godsimulator.rpg.regen

import org.ndk.godsimulator.rpg.profile.RPGProfile

object HealthRegeneration : Regeneration {
    override val id: String = "health"

    override val delay: Long = 2

    override fun regenerate(rpg: RPGProfile) {
        if (rpg.combatTracker.isInCombat) return
        rpg.healProcent(0.2)
    }
}



object StaminaRegeneration : Regeneration {
    override val id: String = "stamina"

    override val delay: Long = 10

    override fun regenerate(rpg: RPGProfile) {
        rpg.profile.fillStamina(2)
    }
}