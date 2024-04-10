@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.language

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.jetbrains.annotations.Contract
import org.ndk.global.tools.Tools
import org.ndk.godsimulator.buying.Wallet
import org.ndk.klib.isBlankOrEmpty
import org.ndk.klib.toBeautifulString
import org.ndk.klib.toSingletonSet
import org.ndk.minecraft.CooldownHolder
import org.ndk.minecraft.command.CommandExecution
import org.ndk.minecraft.command.ServerCommand
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.language.DefaultMSG
import org.ndk.minecraft.language.MSGHolder
import org.ndk.minecraft.language.Message
import java.math.BigInteger
import java.text.DecimalFormat

@Suppress("unused")
object Quick {

    inline fun stop(): Nothing {
        throw ServerCommand.StopCommand()
    }

    inline fun getText(sender: CommandSender, msgID: MSGHolder): String {
        return getMsg(sender, msgID).text
    }

    inline fun getChatText(sender: CommandSender, msgID: MSGHolder): String {
        return getMsg(sender, msgID).chatText
    }

    inline fun getMsg(sender: CommandSender, msgID: MSGHolder): Message {
        return sender.getLangMsg(msgID)
    }

    fun getLangCFG(sender: CommandSender): ConfigurationSection {
        return sender.language.config
    }

    fun <T> isExists(
        collection: Collection<T>,
        obj: T,
        sender: CommandSender,
        onTrue: MSGHolder,
        onFalse: MSGHolder,
        vararg pair: Pair<String, Any>
    ): Boolean {
        val result = collection.contains(obj)
        if (result)
            sender.sendLangMsg(onTrue, *pair)
        else
            sender.sendLangMsg(onFalse, *pair)
        return result
    }

    fun <T> isNotExists(
        collection: Collection<T>,
        o: T,
        sender: CommandSender,
        onTrue: MSGHolder,
        onFalse: MSGHolder,
        vararg pair: Pair<String, Any>
    ): Boolean {
        return !isExists(collection, o, sender, onFalse, onTrue, *pair)
    }

    fun hasBannedSymbols(sender: CommandSender, input: String): Boolean {
        val result = !Tools.hasOnlyLetNumHypUs(input)
        if (result) {
            sender.sendLangMsg(DefaultMSG.HAS_BANNED_SYMBOLS, "string" to input)
            stop()
        }
        return false
    }

    fun tabComplete(associated: Map<Int?, Collection<String?>?>, args: Array<String?>): Collection<String?>? {
        return associated[args.size - 1]
    }

    fun tabComplete(complete: Collection<String>, args: Array<String>): Collection<String>? {
        return if (args.size == 1) complete else null
    }

    fun getOnlinePlayer(sender: CommandSender, name: String): Player {
        val player = Bukkit.getPlayer(name)
        if (player == null) {
            sender.sendLangMsg(DefaultMSG.UNKNOWN_PLAYER, "player" to name)
            stop()
        }
        return player
    }

    @Contract("_, _, _, !null -> !null")
    fun checkInteger(sender: CommandSender, args: Array<String>, argsPosition: Int, def: Int?): Int? {
        if (argsPosition >= args.size) {
            return def
        }
        val intStr = args[argsPosition]
        val number = Tools.defineInt(intStr, null)
        return if (number == null && def == null) {
            sender.sendLangMsg(DefaultMSG.INCORRECT_NUMBER, "number" to intStr)
            stop()
        } else number ?: def
    }

    @Contract("_, _, _, !null -> !null")
    fun checkDouble(sender: CommandSender, args: Array<String>, argsPosition: Int, def: Double?): Double? {
        if (argsPosition >= args.size) {
            return def
        }
        val str = args[argsPosition]
        val number = Tools.defineDouble(str, null)
        return if (number == null && def == null) {
            sender.sendLangMsg(DefaultMSG.INCORRECT_NUMBER, "number" to str)
            throw ServerCommand.StopCommand()
        } else number ?: def
    }

    fun unknownPlayer(sender: CommandSender, playerName: String) {
        sender.sendLangMsg(DefaultMSG.UNKNOWN_PLAYER, "name" to playerName)
    }

    inline fun getOfflinePlayer(sender: CommandSender, offlinePlayerName: String): OfflinePlayer {
        if (offlinePlayerName.isBlankOrEmpty()) {
            unknownPlayer(sender, offlinePlayerName)
            stop()
        }

        @Suppress("DEPRECATION")
        val offlinePlayer: OfflinePlayer = Bukkit.getOfflinePlayer(offlinePlayerName)
        if (offlinePlayer is Player) {
            return offlinePlayer
        }

        if (!offlinePlayer.hasPlayedBefore()) {
            unknownPlayer(sender, offlinePlayerName)
            stop()
        }
        return offlinePlayer
    }


    fun formatSecondsValue(ms: Long, sender: CommandSender): String {
        var leftSeconds = ms.msToSecs()
        val it = getLangCFG(sender)
        val decimalFormat: DecimalFormat = if (leftSeconds >= 5) {
            it.getDecimalFormat(MSG.GTFIVE_SECONDS_FORMAT.id)
        } else {
            if (leftSeconds < 0.1) leftSeconds = 0.1
            it.getDecimalFormat(MSG.LTFIVE_SECONDS_FORMAT.id)
        }
        return decimalFormat.format(leftSeconds)
    }

    fun sendCooldown(
        sender: CommandSender,
        cooldownLeftMs: Long,
        cooldownMSG: MSGHolder = DefaultMSG.COOLDOWN_ON_COMMAND,
        placeholders: Map<String, Iterable<Any>> = emptyMap()
    ) {
        if (placeholders.isEmpty()) {
            sender.sendLangMsg(cooldownMSG, "time" to formatSecondsValue(cooldownLeftMs, sender))
        } else {
            val map = if (placeholders is MutableMap) placeholders else placeholders.toMutableMap()
            map["time"] = formatSecondsValue(cooldownLeftMs, sender).toSingletonSet()
            sender.sendLangMsg(cooldownMSG, map)
        }
    }

    fun checkCooldown(
        sender: CommandSender,
        holder: CooldownHolder,
        cooldownKey: String,
        cooldownMSG: MSGHolder = DefaultMSG.COOLDOWN_ON_COMMAND,
        placeholders: Map<String, Iterable<Any>> = emptyMap()
    ): Boolean {
        val cooldown = holder.getCooldown(cooldownKey)
        if (cooldown != null) {
            sendCooldown(sender, cooldown, cooldownMSG, placeholders)
            return true
        }
        return false
    }

    fun timedError(sender: CommandSender, message: MSGHolder) {
        sender.sendLangMsg(message, "time" to Tools.packDateTime())
    }



    fun notEnoughMoney(sender: CommandSender, price: BigInteger, balance: BigInteger) {
        sender.sendLangMsg(DefaultMSG.NOT_ENOUGH_MONEY, "price" to price.toBeautifulString(), "balance" to balance.toBeautifulString())
    }
    fun notEnoughMoneyAndStop(sender: CommandSender, price: BigInteger, balance: BigInteger) = notEnoughMoney(sender, price, balance).also { stop() }

    fun cannotAfford(sender: CommandSender, price: Wallet, balance: Wallet) {
        val entries = StringBuilder()

        price.forEach { currency, value ->
            val balanceValue = balance.getBalance(currency)
            val placeholder = currency.getFinalPlaceholder(sender)
            placeholder["price"] = value.toBeautifulString().toSingletonSet()
            placeholder["balance"] = balanceValue.toBeautifulString().toSingletonSet()
            entries.append("\n" + SimulatorLangManager.PREFIX + sender.getLangMsg(MSG.CANNOT_AFFORD_ENTRY, placeholder).text)
        }
        sender.sendLangMsg(MSG.CANNOT_AFFORD, "entries" to entries)
    }

    fun priceMessage(sender: CommandSender, price: Wallet): String {
        val entries = StringBuilder()
        price.forEach { currency, value ->
            val placeholder = currency.getFinalPlaceholder(sender)
            placeholder["price"] = value.toBeautifulString().toSingletonSet()
            entries.append("\n" + SimulatorLangManager.PREFIX + sender.getLangMsg(MSG.PRICE_ENTRY, placeholder).text)
        }
        return sender.getLangMsg(MSG.PRICE, "entries" to entries).text
    }

    fun internalError(sender: CommandSender, comment: String) {
        sender.sendLangMsg(DefaultMSG.INTERNAL_ERROR, "time" to Tools.packDateTimeBeautiful(), "comment" to comment)
    }

    fun sendFlagChanged(sender: CommandSender, flagName: String, flagState: Boolean) {
        sender.sendLangMsg(MSG.CMD_ADMIN_FLAG_CHANGED, "flag" to flagName, "state" to flagState)
    }
}


inline fun CommandExecution.sendFlagChange(flag: String, state: Boolean) {
    Quick.sendFlagChanged(sender, flag, state)
}
