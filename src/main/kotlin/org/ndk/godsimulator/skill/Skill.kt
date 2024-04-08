package org.ndk.godsimulator.skill

import org.bukkit.entity.Player
import org.ndk.global.interfaces.Snowflake
import org.ndk.godsimulator.language.MSGNameHolder

interface Skill : MSGNameHolder, Snowflake<String> {
    val executionClass: Class<out SkillExecution>
    val requiredLevel: Int
    val cooldownMs: Long

    fun execute(entity: Player)

    override val defaultPhName: String
        get() = "skill"
}