package org.ndk.godsimulator.god


import org.ndk.godsimulator.GodSimulator
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.plugin.ServerPlugin

class GodsManager : PluginModule {

    override val id: String = "GodsManager"

    val gods = LinkedHashMap<String, God>()


    override fun onLoad(plugin: ServerPlugin) {
        addGod(NotSelectedGod)
        addGod(Zeus)
        addGod(Apollo)
        addGod(Poseidon)
        addGod(Hades)
        addGod(Ares)

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