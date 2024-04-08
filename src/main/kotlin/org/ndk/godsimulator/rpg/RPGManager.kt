@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.rpg


import org.bukkit.command.CommandSender
import org.ndk.global.placeholders.Placeholder
import org.ndk.global.tools.Tools
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.rpg.buff.BuffsList
import org.ndk.godsimulator.rpg.stat.RPGStat
import org.ndk.klib.addById
import org.ndk.klib.getInstanceFieldOrNull
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.plugin.ServerPlugin

class RPGManager : PluginModule {
    override val id: String = "RPGManager"

    val types = HashMap<String, RPGStat<*>>()

    override fun onLoad(plugin: ServerPlugin) {
        val classes = Tools.findClasses(GodSimulator.classLoader, STATS_PACKAGE)
        classes.forEach {
            if (RPGStat::class.java.isAssignableFrom(it)) {
                val instance = it.getInstanceFieldOrNull() as? RPGStat<*> ?: return@forEach
                registerType(instance)
            }
        }

        GodSimulator.rpgManager = this
    }

    override fun onUnload(plugin: ServerPlugin) {
        types.clear()
    }


    fun registerType(type: RPGStat<*>) {
        types.addById(type)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Comparable<T>> getType(id: String): RPGStat<T>? {
        return types[id] as? RPGStat<T> ?: return null
    }

    inline fun getAnyType(id: String): RPGStat<Comparable<Any>>? {
        return getType(id)
    }

    fun formatToLore(buffsLists: BuffsList, player: CommandSender): ArrayList<String> {
        val res = ArrayList<String>()
        buffsLists.forEachStat { typeStr ->
            val type = getAnyType(typeStr) ?: return@forEachStat
            val value = buffsLists.getBuffValueOrNull(type)
            if (value != null) {
                val placeholders = type.getFinalPlaceholder(player)
                placeholders["buff"] = listOf(
                    Placeholder.ofSingle("value", value),
                    Placeholder.ofSingle("name", player.getLangMsg(type.nameMSG).text)
                )
                res.add(player.getLangMsg(type.nameBuffMSG, placeholders).text)
            }
        }
        return res
    }


    companion object {
        const val STATS_PACKAGE = "org.ndk.godsimulator.rpg.stat"
    }
}