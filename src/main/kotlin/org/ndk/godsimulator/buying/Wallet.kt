package org.ndk.godsimulator.buying

import org.ndk.klib.all
import java.math.BigInteger

interface Wallet {
    val map: MutableMap<Currency, BigInteger>

    fun getBalance(currency: Currency): BigInteger
    fun setBalance(currency: Currency, value: BigInteger)
    fun giveBalance(currency: Currency, value: BigInteger)
    fun takeBalance(currency: Currency, value: BigInteger): Boolean
    fun hasBalance(currency: Currency, value: BigInteger): Boolean {
        return getBalance(currency) >= value
    }
    fun resetBalance(currency: Currency) {
        setBalance(currency, BigInteger.ZERO)
    }
    fun resetBalance()



    fun give(wallet: Wallet) {
        wallet.map.forEach(::giveBalance)
    }

    fun take(wallet: Wallet): Boolean {
        if (!has(wallet)) return false
        return wallet.map.all(::takeBalance)
    }

    fun has(wallet: Wallet): Boolean {
        return wallet.map.all(::hasBalance)
    }




    fun transferTo(wallet: Wallet, currency: Currency, value: BigInteger): Boolean {
        if (takeBalance(currency, value)) {
            wallet.setBalance(currency, value)
            return true
        }
        return false
    }

    fun transferTo(wallet: Wallet): Boolean {
        return map.all { (currency, value) ->
            wallet.transferTo(wallet, currency, value)
        }
    }
}