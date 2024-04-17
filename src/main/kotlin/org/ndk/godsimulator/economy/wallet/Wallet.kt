package org.ndk.godsimulator.economy.wallet

import org.ndk.godsimulator.economy.currency.Currency
import org.ndk.godsimulator.rpg.profile.RPGPlayerProfile
import java.math.BigInteger

interface Wallet {
    object EMPTY : WalletImpl() {
        private fun readResolve(): Any = EMPTY
    }

    fun getBalance(currency: Currency): BigInteger
    fun setBalance(currency: Currency, value: BigInteger)

    /**
     * Give balance in the specified currency to the wallet.
     *
     * ProfileWallet implementation also provides scaling via [RPGPlayerProfile.scaleCurrency] method.
     *
     * @param currency the currency to give
     * @param value the value to give
     * @return the final value that was given (scaled value)
     */
    fun giveBalance(currency: Currency, value: BigInteger): BigInteger
    fun takeBalance(currency: Currency, value: BigInteger): Boolean
    fun hasBalance(currency: Currency, value: BigInteger): Boolean {
        return getBalance(currency) >= value
    }
    fun resetBalance(currency: Currency) {
        setBalance(currency, BigInteger.ZERO)
    }
    fun resetBalances()

    fun forEach(action: (Currency, BigInteger) -> Unit)
    fun all(predicate: (Currency, BigInteger) -> Boolean): Boolean



    fun give(wallet: Wallet) {
        wallet.forEach(::giveBalance)
    }

    fun take(wallet: Wallet): Boolean {
        if (!has(wallet)) return false
        return wallet.all(::takeBalance)
    }

    fun has(wallet: Wallet): Boolean {
        return wallet.all(::hasBalance)
    }




    fun transferTo(wallet: Wallet, currency: Currency, value: BigInteger): Boolean {
        if (takeBalance(currency, value)) {
            wallet.setBalance(currency, value)
            return true
        }
        return false
    }

    fun transferTo(wallet: Wallet): Boolean {
        return all { currency: Currency, value: BigInteger ->
            wallet.transferTo(wallet, currency, value)
        }
    }
}