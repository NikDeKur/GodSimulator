package org.ndk.godsimulator.profile

import org.ndk.klib.Constants
import java.math.BigInteger

object LevelManager {
    fun getXpForLevel(level: Int): BigInteger {
        return Constants.BIGINT_100 shl level - 1
    }

    fun giveExperience(profile: PlayerProfile, amount: BigInteger) {
        var newXP = profile.xp + amount
        var xpForNext = getXpForLevel(profile.level + 1)
        while (newXP >= xpForNext) {
            profile.levelUp()
            newXP -= xpForNext
            xpForNext = getXpForLevel(profile.level + 1)
        }
        profile.scopes.xp = newXP
    }
}