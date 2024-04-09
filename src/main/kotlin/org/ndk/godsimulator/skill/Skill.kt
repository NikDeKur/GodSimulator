package org.ndk.godsimulator.skill

import org.bukkit.entity.Player
import org.ndk.global.interfaces.Snowflake
import org.ndk.godsimulator.language.MSGNameHolder

interface Skill : MSGNameHolder, Snowflake<String> {
    val executionClass: Class<out SkillExecution>
    val requiredRebirth: Int
    val requiredLevel: Int
    val cooldownMs: Long

    fun newInstance(executor: Player): SkillExecution {
        val constructor = executionClass.getConstructor(Player::class.java)
        return constructor.newInstance(executor)
    }

    override val defaultPhName: String
        get() = "skill"
}