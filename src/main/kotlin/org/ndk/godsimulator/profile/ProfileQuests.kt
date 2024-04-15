package org.ndk.godsimulator.profile

import com.google.gson.stream.JsonWriter
import org.ndk.global.map.list.ListsHashMap
import org.ndk.godsimulator.GodSimulator.Companion.logger
import org.ndk.godsimulator.database.Database
import org.ndk.godsimulator.language.LangManager
import org.ndk.godsimulator.quest.ProfileQuest
import org.ndk.godsimulator.quest.Quest
import org.ndk.godsimulator.quest.goal.abc.GoalPattern
import org.ndk.godsimulator.quest.goal.impl.Goal
import org.ndk.godsimulator.quest.goal.type.GoalType
import org.ndk.godsimulator.quest.manager.QuestsManager
import org.ndk.klib.forEachSafe
import org.ndk.minecraft.language.MSGHolder
import java.util.*

open class ProfileQuests(val profile: PlayerProfile) {
    //              (string)
    // Actual type is UUID to ProfileQuest
    val data
        get() = profile.scopes.quests




    val goals = ListsHashMap<GoalType<*>, Goal<*>>()

    fun addGoalListener(goal: Goal<*>) {
        goals.add(goal.pattern.type, goal)
    }
    @Suppress("UNCHECKED_CAST")
    fun <T : GoalPattern<T>> getGoals(type: GoalType<T>): List<Goal<T>> {
        return goals[type] as List<Goal<T>>
    }

    fun newQuest(name: MSGHolder, description: MSGHolder, goals: Iterable<Goal<*>>, id: UUID = UUID.randomUUID()): ProfileQuest {
        val profileQuest = ProfileQuest(this, id, profile, name, description)
        goals.forEach(profileQuest::addGoal)
        data[id.toString()] = profileQuest
        return profileQuest
    }

    fun newQuest(quest: Quest, id: UUID = UUID.randomUUID()): ProfileQuest {
        val pQuest = newQuest(quest.nameMSG, quest.descriptionMSG, emptyList(), id)
        quest.goals.forEach(pQuest::addGoal)
        return pQuest
    }


    /**
     * Serialize all quests to JSON.
     *
     * This method is called on each saving by [Database] in [Database.QuestsTypeAdapter].
     */
    fun serialize(writer: JsonWriter) {
        writer.beginObject()

        data.forEach { (id, quest) ->
            quest as ProfileQuest
            val questSection = writer.name(id)
            quest.serialize(questSection)
        }

        writer.endObject()
    }









    // -------------------------
    // Deserialization start
    // -------------------------

    init {
        HashMap(data).forEach { (id, serialized) ->
            val uuid = UUID.fromString(id)
            @Suppress("UNCHECKED_CAST")
            val map = serialized as Map<String, Any>
            deserializeQuest(uuid, map)
        }
    }


    /**
     * Deserialize a quest from serialized data.
     *
     * Also register the quest via [newQuest] method, which will add it to the [data] map.
     *
     * @param id Quest id.
     * @param data Serialized quest data.
     */
    protected fun deserializeQuest(id: UUID, data: Map<String, Any>): ProfileQuest {
        val nameStr = data["name"] as String
        val descriptionStr = data["description"] as String
        @Suppress("UNCHECKED_CAST")
        val goalsMap = data["goals"] as Map<String, List<Map<String, String>>>

        val name = LangManager.getMessage(nameStr) ?: throw IllegalStateException("Failed to deserialize quest name $nameStr")
        val description = LangManager.getMessage(descriptionStr) ?: throw IllegalStateException("Failed to deserialize quest description $descriptionStr")

        val quest = newQuest(name, description, emptyList(), id)

        goalsMap.forEachSafe(::onGoalGroupDeserializeError) { (typeId, serializedGoals) ->
            val type = QuestsManager.getGoalType(typeId) ?: throw IllegalStateException("Failed to deserialize goal type $typeId")

            serializedGoals.forEachSafe(::onGoalDeserializeError) { serializedGoal ->
                val patternDataStr = serializedGoal["data"] ?: throw IllegalStateException("Failed to deserialize goal data")
                val progress = serializedGoal["progress"] ?: throw IllegalStateException("Failed to deserialize goal progress")
                val pattern = type.read(patternDataStr)
                val goal = quest.addGoal(pattern)
                goal.deserialize(progress)
            }
        }

        return quest
    }

    private fun onGoalGroupDeserializeError(typeId: String, serializedGoals: List<Map<String, String>>, e: Exception) {
        logger.warning("Failed to deserialize goal group $typeId")
        logger.warning("Serialized goals: $serializedGoals")
        e.printStackTrace()
    }

    private fun onGoalDeserializeError(serializedGoal: Map<String, String>, e: Exception) {
        logger.warning("Failed to deserialize goal")
        logger.warning("Serialized goal: $serializedGoal")
        e.printStackTrace()
    }

    // -------------------------
    // Deserialization end
    // -------------------------
}