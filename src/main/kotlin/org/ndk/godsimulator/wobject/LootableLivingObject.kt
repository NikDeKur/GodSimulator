package org.ndk.godsimulator.wobject


import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.reward.Reward
import java.math.BigInteger

abstract class LootableLivingObject : RevivingLivingObject() {

    val reward = Reward()

    override fun kill(killer: PlayerProfile.Reference) {
        reward.splitReward<BigInteger>(damageMap)
        super.kill(killer)
    }
}