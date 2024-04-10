package org.ndk.godsimulator.equipable


import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.equipable.impl.AurasManager
import org.ndk.godsimulator.equipable.impl.BagsManager
import org.ndk.godsimulator.equipable.impl.ItemsManager
import org.ndk.godsimulator.equipable.impl.PetsManager
import org.ndk.godsimulator.equipable.type.EquipableTypesManager
import org.ndk.klib.forEachSafe
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.plugin.ServerPlugin
import org.ndk.minecraft.plugin.ServerPlugin.Companion.bLogger
import java.util.*

class EquipableManager : PluginModule {
    override val id: String = "EquipableManager"

    val items: ItemsManager = ItemsManager()
    val pets: PetsManager = PetsManager()
    val auras: AurasManager = AurasManager()
    val bags: BagsManager = BagsManager()

    val managers = LinkedList<EquipableTypesManager<*>>()
        .apply {
            add(items)
            add(pets)
            add(auras)
            add(bags)
        }

    fun onException(manager: EquipableTypesManager<*>, e: Exception) {
        bLogger.warning("Failed to load ${manager.qualifier}")
        e.printStackTrace()
    }

    override fun onLoad(plugin: ServerPlugin) {
        // If one of the managers fails to load, the plugin will load other managers
        // Also prints the stack trace if a manager fails to load
        managers.forEachSafe(::onException) {
            it.load(plugin)
        }

        GodSimulator.equipableManager = this
    }

    override fun onUnload(plugin: ServerPlugin) {
        for (manager in managers) {
            manager.unload()
        }
    }
}