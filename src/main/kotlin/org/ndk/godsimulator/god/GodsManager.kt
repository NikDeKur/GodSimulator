@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.god

import org.ndk.godsimulator.GodSimulator
import org.ndk.minecraft.plugin.ServerPlugin


import org.ndk.minecraft.modules.PluginModule

class GodsManager : PluginModule {

    override val id: String = "GodsManager"

    val gods = LinkedHashMap<String, God>()

    lateinit var notSelectedGod: NotSelectedGod

    override fun onLoad(plugin: ServerPlugin) {
        notSelectedGod = NotSelectedGod()
        addGod(notSelectedGod)
        addGod(Zeus())
        addGod(Apollo())
        addGod(Poseidon())
        addGod(Hades())
        addGod(Ares())

        GodSimulator.godsManager = this
    }

    override fun onUnload(plugin: ServerPlugin) {
        gods.clear()
    }

    fun getGod(id: String): God? {
        return gods[id]
    }

    fun addGod(god: God) {
        gods[god.id] = god
    }

    fun isGod(id: String): Boolean {
        return gods.contains(id)
    }
}