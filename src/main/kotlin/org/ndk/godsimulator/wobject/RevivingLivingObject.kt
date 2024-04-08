package org.ndk.godsimulator.wobject

import org.ndk.godsimulator.GodSimulator.Companion.scheduler
import org.ndk.godsimulator.profile.PlayerProfile

abstract class RevivingLivingObject : LivingObject() {

    var isSpawning: Boolean = false

    abstract val respawnTicks: Long

    open fun scheduleSpawn() {
        if (isSpawning) return
        scheduler.runTaskLater(respawnTicks) {
            spawn()
            isSpawning = false
        }
        isSpawning = true
    }


    override fun kill(killer: PlayerProfile.Reference) {
        super.kill(killer)
        scheduleSpawn()
    }
}