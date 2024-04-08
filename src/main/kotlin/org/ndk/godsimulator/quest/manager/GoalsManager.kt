package org.ndk.godsimulator.quest.manager

import org.bukkit.OfflinePlayer
import org.ndk.global.map.ListsMap
import org.ndk.godsimulator.quest.goal.Goal
import org.ndk.godsimulator.quest.listening.GoalsListener
import org.ndk.godsimulator.quest.listening.SimulatorGoalsListener
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.plugin.ServerPlugin

class GoalsManager : PluginModule {

    override val id: String = "QuestsGoalsManager"

    // TODO: Change to <Class<*>, Map<OfflinePlayer, Goal>>
    val goals = ListsMap<Class<*>, Goal>()

    fun listenGoal(goal: Goal) {
        goals.add(goal.javaClass, goal)
    }

    fun unlistenGoal(goal: Goal) {
        goals.delete(goal.javaClass, goal)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Goal> getGoals(): List<T> {
        return goals[T::class.java] as List<T>
    }

    inline fun <reified T : Goal> getGoals(player: OfflinePlayer): List<T> {
        return getGoals<T>().filter { it.player == player }
    }

    inline fun <reified T : Goal> forEachGoal(player: OfflinePlayer, action: (T) -> Unit) {
        getGoals<T>(player).forEach(action)
    }


    override fun onLoad(plugin: ServerPlugin) {
        plugin.registerListener(GoalsListener(this))
        plugin.registerListener(SimulatorGoalsListener(this))
    }

    override fun onUnload(plugin: ServerPlugin) {
        goals.clear()
    }
}