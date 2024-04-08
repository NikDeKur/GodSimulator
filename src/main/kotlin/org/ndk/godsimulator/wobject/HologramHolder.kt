package org.ndk.godsimulator.wobject

import org.bukkit.entity.Player
import org.ndk.minecraft.hologram.Hologram


interface HologramHolder {
    val hologram: Hologram

    fun spawnHologram() {
        hologram.spawn()
    }
    fun spawnHologram(player: Player) {
        hologram.spawn(player)
    }


    fun updateHologram() {
        hologram.update()
    }
    fun updateHologram(player: Player) {
        hologram.update(player)
    }


    fun removeHologram() {
        hologram.remove()
    }
    fun removeHologram(player: Player) {
        hologram.remove(player)
    }

}