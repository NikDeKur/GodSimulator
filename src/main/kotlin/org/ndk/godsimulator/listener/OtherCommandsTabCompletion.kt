package org.ndk.godsimulator.listener

import org.bukkit.Material
import org.bukkit.block.Biome
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.TabCompleteEvent
import org.ndk.global.tools.Tools
import org.ndk.minecraft.command.CommandExecution


class OtherCommandsTabCompletion : Listener {

    val register = HashMap<String, (CommandExecution) -> List<String>?>()
    init {
        register["//set"] = ::setTabComplete
        register["//setbiome"] = ::biomeTabComplete
        register["//replace"] = ::replaceTabComplete
        register["//cyl"] = ::setTabComplete
    }

    @EventHandler
    fun onTabComplete(event: TabCompleteEvent) {
        val args = event.buffer.split(" ").toMutableList()
        if (args.size < 2) return
        val command = args[0]
        val func = register[command] ?: return
        args.removeAt(0)

        val execution = CommandExecution(event.sender, args.toTypedArray())
        var completions = func(execution)
        if (completions == null) {
            completions = emptyList()
        }

        val matches = ArrayList<String>()
        Tools.copyPartialMatches(args.last(), completions, matches)

        event.completions = matches.ifEmpty { emptyList() }
    }

    val blocks = Material.entries.filter { it.isBlock }.map { it.name.lowercase() }
    fun setTabComplete(execution: CommandExecution): List<String>? {
        return if (execution.args.size == 1) {
            blocks
        } else null
    }

    val biomes = Biome.entries.map { it.name.lowercase() }
    fun biomeTabComplete(execution: CommandExecution): List<String>? {
        if (execution.args.size == 1) {
            return biomes
        }
        return null
    }

    fun replaceTabComplete(execution: CommandExecution): List<String>? {
        val size = execution.args.size
        if (size == 1 || size == 2) {
            return blocks
        }
        return null
    }
}