package org.ndk.godsimulator.economy.wallet

import org.ndk.godsimulator.economy.currency.Currency
import java.math.BigInteger
import java.math.BigInteger.ZERO

/**
 * Class representing wallet, that can store multiple currencies and their values.
 */
open class WalletImpl : HashMap<Currency, BigInteger>(), Wallet {
    override fun getBalance(currency: Currency): BigInteger {
        return get(currency) ?: ZERO
    }

    override fun setBalance(currency: Currency, value: BigInteger) {
        val current = get(currency) ?: ZERO
        put(currency, current + value)
    }

    override fun giveBalance(currency: Currency, value: BigInteger): BigInteger {
        val current = get(currency) ?: ZERO
        val new = current + value
        put(currency, new)
        return new
    }

    override fun takeBalance(currency: Currency, value: BigInteger): Boolean {
        val current = get(currency) ?: return false
        if (current < ZERO) {
            put(currency, ZERO)
            return false
        }
        if (current < value) return false
        put(currency, current - value)
        return true
    }

    override fun resetBalances() {
        clear()
    }

    override fun forEach(action: (Currency, BigInteger) -> Unit) {
        forEach { (currency, value) -> action(currency, value) }
    }

    override fun all(predicate: (Currency, BigInteger) -> Boolean): Boolean {
        return all { (currency, value) -> predicate(currency, value) }
    }
}