package org.ndk.godsimulator.reward

import org.ndk.godsimulator.economy.currency.CurrencyManager
import org.ndk.klib.fromBeautifulString

object RewardTypes {

    object EXPERIENCE : RewardType<ExperienceReward> {
        override val id: String = "experience"
        override fun read(string: String): ExperienceReward {
            return ExperienceReward(fromBeautifulString(string).toBigDecimal().toBigInteger())
        }

        override fun write(data: ExperienceReward): String {
            return data.amount.toString()
        }
    }

    object CURRENCY : RewardType<CurrencyReward> {
        override val id: String = "currency"
        override fun read(string: String): CurrencyReward {
            val (currencyStr, amount) = string.split(" ")
            val currency = CurrencyManager.getCurrency(currencyStr) ?: throw IllegalArgumentException("Currency $currencyStr not found")
            return CurrencyReward(currency, fromBeautifulString(amount).toBigDecimal().toBigInteger())
        }

        override fun write(data: CurrencyReward): String {
            return data.amount.toString()
        }
    }

    @Suppress("ClassName")
    object BAG_FILL : RewardType<BagFillReward> {
        override val id: String = "bag_fill"
        override fun read(string: String): BagFillReward {
            return BagFillReward(fromBeautifulString(string).toBigDecimal().toBigInteger())
        }

        override fun write(data: BagFillReward): String {
            return data.amount.toString()
        }
    }

}