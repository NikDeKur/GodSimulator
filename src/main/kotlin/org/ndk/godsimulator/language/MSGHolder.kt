package org.ndk.godsimulator.language

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.ndk.global.placeholders.Placeholder

interface MSGHolder : Placeholder {
    val defaultPhName: String

    fun getPlaceholder(sender: CommandSender): Iterable<Placeholder> {
        val set = mutableSetOf<Placeholder>()

        if (this is MSGNameHolder)
            set.add(Placeholder.of("name" to getName(sender)))
        if (this is MSGDescriptionHolder)
            set.add(Placeholder.of("description" to getDescription(sender)))

        set.add(this)
        return set
    }

    fun getPairPlaceholder(sender: CommandSender): Pair<String, Iterable<Any>> {
        return defaultPhName to getPlaceholder(sender)
    }

    fun getFinalPlaceholder(sender: CommandSender): MutableMap<String, Iterable<Any>> {
        return mutableMapOf(getPairPlaceholder(sender))
    }

    fun getIcon(player: Player): ItemStack = throw NotImplementedError()
}