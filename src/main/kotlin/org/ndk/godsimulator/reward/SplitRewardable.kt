package org.ndk.godsimulator.reward

import org.ndk.global.map.spread.SpreadMap
import org.ndk.godsimulator.profile.PlayerProfile

interface SplitRewardable<T : Rewardable<T>, N> : Rewardable<T> where N : Number, N : Comparable<N> {
    fun splitReward(map: SpreadMap<PlayerProfile.Reference, N>)
}