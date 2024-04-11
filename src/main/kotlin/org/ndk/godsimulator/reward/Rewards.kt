package org.ndk.godsimulator.reward

import org.ndk.global.map.spread.SpreadMap
import org.ndk.godsimulator.profile.PlayerProfile
import java.math.BigInteger

open class ExperienceReward(val amount: BigInteger) : SplitRewardable<BigInteger> {
    override fun reward(profile: PlayerProfile) {
        profile.giveExperience(amount)
    }

    override fun splitReward(map: SpreadMap<PlayerProfile.Reference, BigInteger>) {
        map.split(amount) { ref, value ->
            ref
                .profile
                .thenAccept {
                    it.giveExperience(value)
                }
        }
    }
}


open class CurrencyReward(val amount: BigInteger, val func: (PlayerProfile, BigInteger) -> Unit) : SplitRewardable<BigInteger> {
    override fun reward(profile: PlayerProfile) {
        func(profile, amount)
    }

    override fun splitReward(map: SpreadMap<PlayerProfile.Reference, BigInteger>) {
        map.split(amount) { ref, value ->
            ref
                .profile
                .thenAccept {
                    func(it, value)
                }
        }
    }
}

open class CoinsReward(amount: BigInteger) : CurrencyReward(amount, PlayerProfile::giveCoins)
open class SoulsReward(amount: BigInteger) : CurrencyReward(amount, PlayerProfile::giveSouls)


class BagFillReward(val amount: BigInteger) : SplitRewardable<BigInteger> {
    override fun reward(profile: PlayerProfile) {
        profile.fillBag(amount)
    }

    override fun splitReward(map: SpreadMap<PlayerProfile.Reference, BigInteger>) {
        map.split(amount) { ref, value ->
            ref
                .profile
                .thenAccept {
                    it.fillBag(value)
                }
        }
    }
}