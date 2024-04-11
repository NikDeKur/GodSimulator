package org.ndk.godsimulator.skill

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.ndk.global.interfaces.Snowflake
import org.ndk.godsimulator.extension.setTexture
import org.ndk.godsimulator.god.God
import org.ndk.godsimulator.language.MSGDescriptionHolder
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.minecraft.item.ItemPattern

interface Skill : MSGNameHolder, MSGDescriptionHolder, Snowflake<String> {
    val god: God
    val skillId: String
    val executionClass: Class<out SkillExecution>
    val requiredRebirth: Int
    val requiredLevel: Int
    val cooldownMs: Long

    override val id: String
        get() = "${god.id}_$skillId"

    fun newInstance(executor: Player): SkillExecution {
        val constructor = executionClass.getConstructor(Player::class.java)
        return constructor.newInstance(executor)
    }

    override fun getIcon(player: Player): ItemStack {
        return ItemPattern.from(Material.BARRIER)
            .setDisplayName(nameMSG)
            .setLore(descriptionMSG)
            .setTag("god", id)
            .setTexture(id)
            .build(player, getFinalPlaceholder(player))
    }

    override val defaultPhName: String
        get() = "skill"
}