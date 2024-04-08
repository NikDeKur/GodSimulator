package org.ndk.godsimulator.profile

import org.ndk.global.placeholders.Placeholder
import org.ndk.godsimulator.buying.Currency
import org.ndk.godsimulator.buying.Wallet
import org.ndk.klib.toBeautifulString
import java.math.BigInteger
import java.math.BigInteger.ZERO

class ProfileWallet(val profile: PlayerProfile) : Wallet , Placeholder {


    override val placeholderMap: MutableMap<String, Any>
        get() = object : HashMap<String, Any>() {
            init {
                wallet.keys.forEach {
                    val currency = Currency.currencies[it]
                    if (currency != null)
                         put(it, getBalance(currency).toBeautifulString())
                }
            }

            override fun get(key: String): Any {
                val currency = Currency.currencies[key] ?: return ZERO
                return getBalance(currency).toBeautifulString()
            }
        }

    val scopes = profile.scopes
    val wallet by scopes::wallet

    override fun getBalance(currency: Currency): BigInteger {
        val value = wallet[currency.id] ?: return ZERO
        if (value is BigInteger) return value
        val bigInt = value.toString().toBigIntegerOrNull() ?: ZERO
        if (bigInt > ZERO)
            setBalance(currency, bigInt)
        return bigInt
    }

    override fun setBalance(currency: Currency, value: BigInteger) {
        if (value <= ZERO) {
            wallet.remove(currency.id)
        } else {
            wallet[currency.id] = value
        }
    }

    override fun giveBalance(currency: Currency, value: BigInteger) {
        setBalance(currency, getBalance(currency) + value)
    }

    override fun takeBalance(currency: Currency, value: BigInteger): Boolean {
        val balance = getBalance(currency)
        if (balance < value) return false
        setBalance(currency, balance - value)
        return true
    }

    override fun resetBalances() {
        wallet.clear()
    }

    @Suppress("LABEL_NAME_CLASH")
    override fun forEach(action: (Currency, BigInteger) -> Unit) {
        wallet.forEach { (key, value) ->
            val currency = Currency.currencies[key] ?: return@forEach
            action(currency, value as BigInteger)
        }
    }

    override fun all(predicate: (Currency, BigInteger) -> Boolean): Boolean {
        return wallet.map { (key, value) ->
            val currency = Currency.currencies[key] ?: return@map false
            predicate(currency, value as BigInteger)
        }.all { it }
    }



    fun getCoins() = getBalance(Currency.COINS)
    fun setCoins(value: BigInteger) = setBalance(Currency.COINS, value)
    fun giveCoins(value: BigInteger) = giveBalance(Currency.COINS, value)
    fun takeCoins(value: BigInteger) = takeBalance(Currency.COINS, value)
    fun hasCoins(value: BigInteger) = hasBalance(Currency.COINS, value)
    fun resetCoins() = resetBalance(Currency.COINS)

    fun getSouls() = getBalance(Currency.SOULS)
    fun setSouls(value: BigInteger) = setBalance(Currency.SOULS, value)
    fun giveSouls(value: BigInteger) = giveBalance(Currency.SOULS, value)
    fun takeSouls(value: BigInteger) = takeBalance(Currency.SOULS, value)
    fun hasSouls(value: BigInteger) = hasBalance(Currency.SOULS, value)
    fun resetSouls() = resetBalance(Currency.SOULS)
}