package org.ndk.godsimulator.extension

import org.bukkit.entity.Player
import org.ndk.godsimulator.location.SimulatorLocation
import org.ndk.minecraft.extension.blockLocation


inline val Player.simulatorLocation: SimulatorLocation?
    get() = this.blockLocation.simulatorLocation
