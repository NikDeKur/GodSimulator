package org.ndk.godsimulator.rpg.regen

import org.bukkit.scheduler.BukkitTask
import org.ndk.godsimulator.GodSimulator.Companion.scheduler
import org.ndk.godsimulator.database.Database.Companion.loadedAccessor
import org.ndk.klib.addById
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.plugin.ServerPlugin

object RegenerationManager : PluginModule {
    override val id: String = "RegenerationManager"

    val regenerations = HashMap<String, Regeneration>()
    val regenerationTasks = HashMap<String, BukkitTask>()

    override fun onLoad(plugin: ServerPlugin) {
        addRegeneration(HealthRegeneration)
        addRegeneration(StaminaRegeneration)
    }

    override fun onUnload(plugin: ServerPlugin) {
        regenerations.clear()
        regenerationTasks.values.forEach(BukkitTask::cancel)
        regenerationTasks.clear()
    }

    fun addRegeneration(regen: Regeneration) {
        regenerations.addById(regen)
        regenerationTasks[regen.id] = scheduler.runTaskTimer(regen.delay) { tick(regen) }
    }


    fun tick(regen: Regeneration) {
        for (player in ServerPlugin.online) {
            val rpg = player.loadedAccessor?.selectedProfile?.rpg ?: continue
            regen.regenerate(rpg)
        }
    }


}