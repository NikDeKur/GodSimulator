package org.ndk.godsimulator.skill.cast

import org.bukkit.entity.Player
import org.ndk.godsimulator.database.Database.Companion.accessor
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.language.Quick
import org.ndk.minecraft.extension.sendLangMsg

object CastManager {

    fun cast(player: Player, index: Int): Boolean {
        val profile = player.accessor.profile
        val god = profile.god
        val skill = god.skills.getOrNull(index)
        if (skill != null) {
            val cooldown = profile.getCooldown(skill.id)
            if (cooldown != null && cooldown > 0) {
                Quick.sendCooldown(player, cooldown, MSG.SKILL_CAST_COOLDOWN, skill.getFinalPlaceholder(player))
                return false
            }

            if (skill.requiredLevel > profile.level) {
                player.sendLangMsg(MSG.SKILL_REQUIRE_LEVEL, skill.getFinalPlaceholder(player))
                return false
            }

            skill.execute(player)
            profile.setCooldown(skill.id, skill.cooldownMs)
            return true
        }
        return false
    }
}