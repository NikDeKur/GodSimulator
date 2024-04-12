package org.ndk.godsimulator.equipable

import org.ndk.godsimulator.language.MSG
import org.ndk.minecraft.language.MSGHolder

enum class Rarity(val display: MSGHolder) {
    COMMON(MSG.RARITY_NAME_COMMON),
    UNCOMMON(MSG.RARITY_NAME_UNCOMMON),
    RARE(MSG.RARITY_NAME_RARE),
    EPIC(MSG.RARITY_NAME_EPIC),
    MYTHIC(MSG.RARITY_NAME_MYTHIC),
    LEGENDARY(MSG.RARITY_NAME_LEGENDARY),
    SPECIAL(MSG.RARITY_NAME_SPECIAL)
}
