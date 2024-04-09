package org.ndk.godsimulator.event.profile

import org.bukkit.event.Event
import org.ndk.godsimulator.profile.PlayerProfile

abstract class ProfileEvent(val profile: PlayerProfile) : Event()