package org.ndk.godsimulator.language

import org.bukkit.command.CommandSender
import org.ndk.godsimulator.extension.languageAsync
import org.ndk.minecraft.extension.getLangMsg
import java.util.concurrent.CompletableFuture

interface MSGNameHolder : MSGHolder {

    val nameMSG: org.ndk.minecraft.language.MSGHolder

    fun getName(sender: CommandSender): String {
        return sender.getLangMsg(nameMSG).text
    }

    fun getNameAsync(sender: CommandSender): CompletableFuture<String> {
        return sender.languageAsync.thenApply { it[nameMSG].text }
    }
}