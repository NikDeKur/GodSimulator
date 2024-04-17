package org.ndk.godsimulator.reward

import org.ndk.godsimulator.profile.PlayerProfile

interface Rewardable<T : Rewardable<T>> {
    val type: RewardType<T>
    fun reward(profile: PlayerProfile)
}