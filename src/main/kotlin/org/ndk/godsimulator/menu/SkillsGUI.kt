package org.ndk.godsimulator.menu

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profile
import org.ndk.godsimulator.skill.Skill
import org.ndk.godsimulator.skill.cast.SkillListener
import org.ndk.klib.toSingletonSet
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.gui.GUI
import org.ndk.minecraft.gui.GUIFlag
import org.ndk.minecraft.item.Patterns

class SkillsGUI(player: Player) : GUI(player, 27) {

    override val flags: Set<GUIFlag> = setOf(GUIFlag.CANNOT_TAKE, GUIFlag.CANNOT_PUT)

    override fun getTitle(): String {
        return player.getLangMsg(MSG.MENU_SKILLS_TITLE).text
    }

    fun getIcon(skill: Skill): ItemStack {
        val profile = player.profile
        val skills = profile.skills
        val icon = skill.getIcon(player)
        val isUnlocked = skills.isUnlocked(skill)
        val isBinded = skills.isBinded(skill)

        val lore = ArrayList<String>()
        lore.add("")
        if (isUnlocked) {
            if (isBinded) {
                val bind = (skills.getBind(skill) ?: -1) + 1
                lore.add(player.getLangMsg(MSG.SKILL_UNBIND, "bind" to bind).text)
            }
            lore.add(player.getLangMsg(MSG.SKILL_BIND).text)
        } else {
            lore.add(player.getLangMsg(MSG.SKILL_REQUIRE_LEVEL, skill.getFinalPlaceholder(player)).text)
        }
        icon.addLore(lore)

        return icon
    }

    override fun beforeOpen() {
        val profile = player.profile
        val god = profile.god

        inventory.setColumn(1, Patterns.EMPTY_SLOT)
        inventory.setColumn(4, Patterns.EMPTY_SLOT)
        inventory.setColumn(6, Patterns.EMPTY_SLOT)
        inventory.setColumn(9, Patterns.EMPTY_SLOT)
        inventory.setItem(4, Patterns.EMPTY_SLOT)
        inventory.setItem(22, Patterns.EMPTY_SLOT)

        val godIcon = god.getIcon(player)
        inventory.setItem(13, godIcon)

        god.skills.values.forEachIndexed { index, skill ->
            val skillIcon = getIcon(skill)
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

    override fun onClick(event: InventoryClickEvent) {
        val item = event.currentItem
        if (item.type.isAir) return
        val skillId = item.getStringTag("skillId") ?: return
        val profile = player.profile
        val skills = profile.skills
        val skill = profile.god.skills[skillId] ?: return
        if (!skills.isUnlocked(skill)) return

        if (event.isRightClick) {
            val bindAt = skills.getBind(skill) ?: return
            skills.unbind(bindAt)
            update()
        } else if (event.isLeftClick) {
            player.sendLangMsg(MSG.SKILL_BIND_CLICK_SLOT)
            SkillListener.waitBind(player, skill)
            closeAndFinish()
        }
    }

    companion object {

        fun bind(player: Player, skill: Skill, slot: Int) {
            val profile = player.profile
            val skills = profile.skills
            if (skills.isBinded(skill)) {
                skills.unbind(slot)
            }

            skills.bind(slot, skill)
            val placeholder = skill.getFinalPlaceholder(player)
            placeholder["slot"] = (slot + 1).toSingletonSet()
            player.sendLangMsg(MSG.SKILL_BIND_SUCCESS, placeholder)
            SkillsGUI(player).open()
        }
    }
}