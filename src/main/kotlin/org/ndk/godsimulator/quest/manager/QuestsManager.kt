package org.ndk.godsimulator.quest.manager

import org.ndk.godsimulator.quest.PlayerQuest
import org.ndk.minecraft.plugin.ServerPlugin
import org.ndk.minecraft.modules.PluginModule

class QuestsManager : PluginModule {
    override val id: String = "QuestsManager"

    val quests = ArrayList<PlayerQuest>()


    fun addQuest(playerQuest: PlayerQuest) {
        quests.add(playerQuest)
        for (objective in playerQuest.objectives) {
            for (goal in objective.goals) {
                goals.listenGoal(goal)
            }
        }
    }


    fun removeQuest(playerQuest: PlayerQuest) {
        quests.remove(playerQuest)
        for (objective in playerQuest.objectives) {
            for (goal in objective.goals) {
                goals.unlistenGoal(goal)
            }
        }
    }




    val goals = GoalsManager()

    override fun onLoad(plugin: ServerPlugin) {
        goals.onLoad(plugin)
    }
}