package org.ndk.godsimulator.menu

import org.bukkit.entity.Player
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profile
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.gui.GUI

class SkillsGUI(player: Player) : GUI(player, 27) {
    override fun getTitle(): String {
        return player.getLangMsg(MSG.MENU_SKILLS_TITLE).text
    }

    override fun beforeOpen() {
        val profile = player.profile
        val skills = profile.skills
        val god = profile.god

        val godIcon = god.getIcon(player)
        inventory.setItem(13, godIcon)

        god.skills.forEachIndexed { index, skill ->
            val skillIcon = skill.getIcon(player)
            val pos = when (index) {
                0 -> 1
                1 -> 2
                2 -> 9
                3 -> 10
                4 -> 18
                5 -> 19
                else -> return
            }
            inventory.setItem(pos, skillIcon)
        }
    }
}