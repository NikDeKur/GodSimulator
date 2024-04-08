package org.ndk.godsimulator.reward

import org.ndk.global.map.spread.SpreadMap
import org.ndk.godsimulator.profile.PlayerProfile

interface SplitRewardable<T> : Rewardable where T : Number, T : Comparable<T> {
    fun splitReward(map: SpreadMap<PlayerProfile.Reference, T>)
}