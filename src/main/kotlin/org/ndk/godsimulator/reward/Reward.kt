package org.ndk.godsimulator.reward

import com.google.gson.stream.JsonWriter
import org.ndk.global.map.spread.SpreadMap
import org.ndk.godsimulator.database.EasyTypeAdapter
import org.ndk.godsimulator.profile.PlayerProfile
import java.util.*

class Reward {

    val rewards = LinkedList<Rewardable<*>>()

    fun addReward(reward: Rewardable<*>) {
        rewards.add(reward)
    }

    fun giveReward(profile: PlayerProfile) {
        rewards.forEach { it.reward(profile) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> splitReward(map: SpreadMap<PlayerProfile.Reference, T>) where T : Number, T : Comparable<T> {
        rewards.forEach {
            if (it !is SplitRewardable<*, *>) return@forEach
            (it as SplitRewardable<*, T>).splitReward(map)
        }
    }


    companion object {
        val EMPTY = Reward()

        @Suppress("UNCHECKED_CAST")
        fun <T : Rewardable<T>> write(writer: JsonWriter, rewardable: Rewardable<T>, reward: Rewardable<*>) {
            writer.beginObject()
            writer.name("type").value(rewardable.type.id)
            writer.name("data").value(rewardable.type.write(reward as T))
            writer.endObject()
        }

        val TypeAdapter = EasyTypeAdapter(
            Reward::class.java,
            { out, value ->
                out.beginArray()
                value.rewards.forEach {
                    write(out, it, it)
                }
                out.endArray()
            },

            {
                val reward = Reward()
                it.beginArray()
                while (it.hasNext()) {
                    val typeStr = it.nextString()
                    val data = it.nextString()
                    val type = RewardsManager.getRewardType(typeStr)
                    if (type != null)
                        reward.addReward(type.read(data))
                }
                reward
            }
        )
    }
}