package org.ndk.godsimulator.god


import org.ndk.godsimulator.GodSimulator
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.plugin.ServerPlugin

object GodsManager : PluginModule {

    val gods = LinkedHashMap<String, God>()


    override fun onLoad(plugin: ServerPlugin) {
        addGod(NotSelectedGod)
        addGod(Zeus)
        addGod(Apollo)
        addGod(Poseidon)
        addGod(Hades)
        addGod(Ares)
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