package org.ndk.godsimulator.reward

import org.ndk.global.interfaces.Snowflake

interface RewardType<T : Rewardable<T>> : Snowflake<String> {

    fun read(string: String): T
    fun write(data: T): String
}