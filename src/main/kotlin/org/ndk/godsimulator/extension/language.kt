@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.extension

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.ndk.godsimulator.GodSimulator.Companion.instance
import org.ndk.godsimulator.database.Database.accessorAsync
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.minecraft.language.Language
import org.ndk.minecraft.language.MSGHolder
import org.ndk.minecraft.language.Message
import java.util.concurrent.CompletableFuture


inline fun PlayerProfile.getLangMsg(msg: MSGHolder): Message {
    return language[msg]
}
fun PlayerProfile.getLangMsg(msg: MSGHolder, placeholders: Map<String, Iterable<Any>>): Message {
    return getLangMsg(msg).parsePlaceholders(placeholders)
}
fun PlayerProfile.getLangMsg(msg: MSGHolder, vararg placeholders: Pair<String, Any>): Message {
    return getLangMsg(msg).parsePlaceholders(*placeholders)
}



inline fun PlayerProfile.sendLangMsg(msg: MSGHolder) {
    this.onlinePlayer?.let { getLangMsg(msg).send(it) }
}
inline fun PlayerProfile.sendLangMsg(msg: MSGHolder, placeholders: Map<String, Iterable<Any>> = emptyMap()) {
    this.onlinePlayer?.let { getLangMsg(msg, placeholders).send(it) }
}
inline fun PlayerProfile.sendLangMsg(msg: MSGHolder, vararg placeholders: Pair<String, Any>) {
    this.onlinePlayer?.let { getLangMsg(msg, *placeholders).send(it) }
}




val CommandSender.languageAsync: CompletableFuture<Language>
    get() = (this as? Player)?.accessorAsync?.thenApply {
        it.profile.language
    } ?: instance.languagesManager.defaultLangFuture