package org.ndk.godsimulator.wobject.damage

import org.ndk.global.map.spread.SpreadBigIntegerMap
import org.ndk.godsimulator.profile.PlayerProfile
import java.math.BigInteger

class DamageMap(
    max: BigInteger,
    onMax: (PlayerProfile.Reference) -> Unit
) : SpreadBigIntegerMap<PlayerProfile.Reference>(max, onMax)