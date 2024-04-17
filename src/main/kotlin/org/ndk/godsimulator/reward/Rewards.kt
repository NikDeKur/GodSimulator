package org.ndk.godsimulator.reward

import org.ndk.global.map.spread.SpreadMap
import org.ndk.godsimulator.economy.currency.Currency
import org.ndk.godsimulator.profile.PlayerProfile
import java.math.BigInteger

open class ExperienceReward(val amount: BigInteger) : SplitRewardable<ExperienceReward, BigInteger> {
    override val type = RewardTypes.EXPERIENCE
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


open class CurrencyReward(val currency: Currency, val amount: BigInteger) : SplitRewardable<CurrencyReward, BigInteger> {
    override val type = RewardTypes.CURRENCY
    override fun reward(profile: PlayerProfile) {
        profile.wallet.giveBalance(currency, amount)
    }

    override fun splitReward(map: SpreadMap<PlayerProfile.Reference, BigInteger>) {
        map.split(amount) { ref, value ->
            ref
                .profile
                .thenAccept {
                    it.wallet.giveBalance(currency, value)
                }
        }
    }
}

open class CoinsReward(amount: BigInteger) : CurrencyReward(Currency.COINS, amount)
open class SoulsReward(amount: BigInteger) : CurrencyReward(Currency.SOULS, amount)


class BagFillReward(val amount: BigInteger) : SplitRewardable<BagFillReward, BigInteger> {
    override val type = RewardTypes.BAG_FILL
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