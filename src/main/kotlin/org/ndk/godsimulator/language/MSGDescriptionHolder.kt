package org.ndk.godsimulator.language

import org.ndk.godsimulator.extension.languageAsync
import org.ndk.minecraft.extension.getLangMsg

interface MSGDescriptionHolder : MSGHolder {

    val descriptionMSG: org.ndk.minecraft.language.MSGHolder

    fun getDescription(sender: org.bukkit.command.CommandSender): String {
        return sender.getLangMsg(descriptionMSG).text
    }

    fun getDescriptionAsync(sender: org.bukkit.command.CommandSender): java.util.concurrent.CompletableFuture<String> {
        return sender.languageAsync.thenApply { it[descriptionMSG].text }
    }
}