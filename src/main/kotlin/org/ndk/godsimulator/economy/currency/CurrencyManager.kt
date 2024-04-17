package org.ndk.godsimulator.economy.currency

import org.ndk.klib.addById
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.plugin.ServerPlugin

object CurrencyManager : PluginModule {
    val currencies = HashMap<String, Currency>()

    fun addCurrency(currency: Currency) {
        currencies.addById(currency)
    }

    fun getCurrency(id: String): Currency? {
        return currencies[id]
    }

    override fun onLoad(plugin: ServerPlugin) {
        currencies.addById(Currency.COINS)
        currencies.addById(Currency.SOULS)
    }

    override fun onUnload(plugin: ServerPlugin) {
        currencies.clear()
    }
}