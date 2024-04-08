package org.ndk.godsimulator.reward

import org.ndk.godsimulator.profile.PlayerProfile

interface Rewardable {
    fun reward(profile: PlayerProfile)
}