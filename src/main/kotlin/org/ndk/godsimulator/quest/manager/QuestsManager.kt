package org.ndk.godsimulator.quest.manager

import org.ndk.godsimulator.extension.readMSGHolderOrThrow
import org.ndk.godsimulator.quest.Quest
import org.ndk.godsimulator.quest.goal.type.GoalType
import org.ndk.godsimulator.quest.goal.type.GoalTypes
import org.ndk.minecraft.extension.forEachSectionSafe
import org.ndk.minecraft.extension.getSectionOrThrow
import org.ndk.minecraft.extension.pairs
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.plugin.ServerPlugin

object QuestsManager : PluginModule {

    override val id: String = "QuestsGoalsManager"


    val goalTypes = HashMap<String, GoalType<*>>()
    fun addGoalType(type: GoalType<*>) {
        goalTypes[type.id] = type
    }
    fun getGoalType(id: String): GoalType<*>? {
        return goalTypes[id]
    }


    val quests = HashMap<String, Quest>()
    fun addQuest(quest: Quest) {
        quests[quest.id] = quest
    }
    fun getQuest(id: String): Quest? {
        return quests[id]
    }


    override fun onLoad(plugin: ServerPlugin) {
        GoalTypes.register(this)

        val config = plugin.configsManager.load("quests")
        config.forEachSectionSafe { sec ->
            val name = sec.readMSGHolderOrThrow("name")
            val description = sec.readMSGHolderOrThrow("description")
            val goalPatterns = sec.getSectionOrThrow("goals").pairs

            val goals = goalPatterns.entries.mapNotNull {
                val id = it.key
                val data = it.value as? String ?: return@mapNotNull null
                val type = getGoalType(id) ?: return@mapNotNull null
                try {
                    type.read(data)
                } catch (e: Exception) {
                    plugin.logger.warning("Failed to read goal pattern $id")
                    e.printStackTrace()
                    return@mapNotNull null
                }
            }

            val quest = Quest(sec.name, name, description)
            goals.forEach(quest::addGoal)
            addQuest(quest)
        }
    }

    override fun onUnload(plugin: ServerPlugin) {
        goalTypes.clear()
        quests.clear()
    }



}