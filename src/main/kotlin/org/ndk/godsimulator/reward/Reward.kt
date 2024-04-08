package org.ndk.godsimulator.reward

import org.ndk.global.map.spread.SpreadMap
import org.ndk.godsimulator.profile.PlayerProfile
import java.util.*

class Reward {

    val rewards = LinkedList<Rewardable>()

    fun addReward(reward: Rewardable) {
        rewards.add(reward)
    }

    fun giveReward(profile: PlayerProfile) {
        rewards.forEach { it.reward(profile) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> splitReward(map: SpreadMap<PlayerProfile.Reference, *>) where T : Number, T : Comparable<T> {
        rewards.forEach {
            if (it !is SplitRewardable<*>) return@forEach
            (it as SplitRewardable<T>).splitReward(map as SpreadMap<PlayerProfile.Reference, T>)
        }
    }
}