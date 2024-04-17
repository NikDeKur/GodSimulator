package org.ndk.godsimulator.reward

import org.ndk.klib.addById
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.plugin.ServerPlugin

object RewardsManager : PluginModule {
    override val id: String = "RewardsManager"

    val rewardTypes = mutableMapOf<String, RewardType<*>>()

    fun addRewardType(type: RewardType<*>) {
        rewardTypes[type.id] = type
    }

    fun getRewardType(id: String): RewardType<*>? {
        return rewardTypes[id]
    }

    override fun onLoad(plugin: ServerPlugin) {
        rewardTypes.addById(RewardTypes.EXPERIENCE)
        rewardTypes.addById(RewardTypes.CURRENCY)
    }

    override fun onUnload(plugin: ServerPlugin) {
        rewardTypes.clear()
    }
}