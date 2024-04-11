package org.ndk.godsimulator.god

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.ndk.global.interfaces.Snowflake
import org.ndk.global.placeholders.Placeholder
import org.ndk.godsimulator.extension.setTexture
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.language.MSGDescriptionHolder
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.godsimulator.profile.ProfileSkills
import org.ndk.godsimulator.skill.Skill
import org.ndk.godsimulator.skill.SkillExecution
import org.ndk.minecraft.item.ItemPattern
import org.ndk.minecraft.language.MSGHolder


abstract class God: Placeholder, MSGNameHolder, MSGDescriptionHolder, Snowflake<String> {
    override val defaultPhName: String = "god"

    open val isSelectable: Boolean = true

    abstract val skills: Map<String, Skill>

    override fun getIcon(player: Player): ItemStack {
        return ItemPattern.from(Material.BARRIER)
            .setDisplayName(nameMSG)
            .setLore(descriptionMSG)
            .setTag("god", id)
            .setTexture(id)
            .build(player, getFinalPlaceholder(player))
    }

    open fun updateUnlockedSkills(skills: ProfileSkills, bind : Boolean = false) {
        val profile = skills.profile
        this.skills.values.forEachIndexed { index, skill ->
            if (profile.level < skill.requiredLevel) return
            if (profile.rebirth < skill.requiredRebirth) return
            skills.unlock(skill)
            if (bind)
                skills.bind(index, skill)
        }
    }
}


object NotSelectedGod : God() {
    override val id: String = "NotSelected"
    override val nameMSG = MSG.GOD_NOT_SELECTED_NAME
    override val descriptionMSG = MSG.GOD_NOT_SELECTED_DESCRIPTION
    override val isSelectable: Boolean = false
    override val skills = emptyMap<String, Skill>()

    // Do nothing
    override fun updateUnlockedSkills(skills: ProfileSkills, bind: Boolean) {}
}

object Zeus : God() {
    override val id: String = "zeus"
    override val nameMSG = MSG.GOD_ZEUS_NAME
    override val descriptionMSG = MSG.GOD_ZEUS_DESCRIPTION
    override val skills = Skills.entries.associateBy { it.name }

    enum class Skills(
        override val executionClass: Class<out SkillExecution>,
        override val nameMSG: MSG,
        override val descriptionMSG: MSGHolder,
        override val requiredLevel: Int,
        override val requiredRebirth: Int,
        override val cooldownMs: Long
    ) : Skill {
        THUNDERBOLT(Zeus_ThunderBolt::class.java, MSG.SKILL_ZEUS_THUNDERBOLT_NAME, MSG.SKILL_ZEUS_THUNDERBOLT_DESCRIPTION, 0, 0, 1000),
        EARTHQUAKE(Zeus_Earthquake::class.java, MSG.SKILL_ZEUS_EARTHQUAKE_NAME, MSG.SKILL_ZEUS_EARTHQUAKE_DESCRIPTION, 10, 0, 3 * 1000),
        ;

        override val god: God = Zeus
        override val skillId: String = name
    }
}




object Apollo : God() {
    override val id: String = "apollo"
    override val nameMSG = MSG.GOD_APOLLO_NAME
    override val descriptionMSG = MSG.GOD_APOLLO_DESCRIPTION
    override val skills = Skills.entries.associateBy { it.name }

    enum class Skills(
        override val executionClass: Class<out SkillExecution>,
        override val nameMSG: MSG,
        override val descriptionMSG: MSGHolder,
        override val requiredLevel: Int,
        override val requiredRebirth: Int,
        override val cooldownMs: Long
    ) : Skill {
        SUNBEAM(Apollo_SunBeam::class.java, MSG.SKILL_APOLLO_SUNBEAM_NAME, MSG.SKILL_APOLLO_SUNBEAM_DESCRIPTION, 0, 0, 1000),
        SOLARBLAST(Apollo_SolarBlast::class.java, MSG.SKILL_APOLLO_SOLARBLAST_NAME, MSG.SKILL_APOLLO_SOLARBLAST_DESCRIPTION, 10, 0, 3 * 1000),
        ;

        override val god: God = Apollo
        override val skillId: String = name
    }
}

object Poseidon : God() {
    override val id: String = "poseidon"
    override val nameMSG = MSG.GOD_POSEIDON_NAME
    override val descriptionMSG = MSG.GOD_POSEIDON_DESCRIPTION
    override val skills = Skills.entries.associateBy { it.name }

    enum class Skills(
        override val executionClass: Class<out SkillExecution>,
        override val nameMSG: MSG,
        override val descriptionMSG: MSGHolder,
        override val requiredRebirth: Int,
        override val requiredLevel: Int,
        override val cooldownMs: Long
    ) : Skill {
        // WATERFALL(Poseidon_Waterfall::class.java, MSG.SKILL_POSEIDON_WATERFALL_NAME, 0, 0, 1000),
        // TIDALWAVE(Poseidon_TidalWave::class.java, MSG.SKILL_POSEIDON_TIDALWAVE_NAME, 10, 0, 3 * 1000),
        ;

        override val god: God = Poseidon
        override val skillId: String = name
    }
}

// Аид
object Hades : God() {
    override val id: String = "hades"
    override val nameMSG = MSG.GOD_HADES_NAME
    override val descriptionMSG = MSG.GOD_HADES_DESCRIPTION
    override val skills = Skills.entries.associateBy { it.name }

    enum class Skills(
        override val executionClass: Class<out SkillExecution>,
        override val nameMSG: MSG,
        override val descriptionMSG: MSGHolder,
        override val requiredLevel: Int,
        override val requiredRebirth: Int,
        override val cooldownMs: Long
    ) : Skill {
        // DEATHRAY(Hades_DeathRay::class.java, MSG.SKILL_HADES_DEATHRAY_NAME, 0, 0, 1000),
        // SOULSUCKER(Hades_SoulSucker::class.java, MSG.SKILL_HADES_SOULSUCKER_NAME, 10, 0, 3 * 1000),
        ;

        override val god: God = Hades
        override val skillId: String = name
    }

}


object Ares : God() {
    override val id: String = "ares"
    override val nameMSG = MSG.GOD_ARES_NAME
    override val descriptionMSG = MSG.GOD_ARES_DESCRIPTION
    override val skills = Skills.entries.associateBy { it.name }

    enum class Skills(
        override val executionClass: Class<out SkillExecution>,
        override val nameMSG: MSG,
        override val descriptionMSG: MSGHolder,
        override val requiredLevel: Int,
        override val requiredRebirth: Int,
        override val cooldownMs: Long
    ) : Skill {
        // BLOODLUST(Ares_BloodLust::class.java, MSG.SKILL_ARES_BLOODLUST_NAME, 0, 0, 1000),
        // WARCRY(Ares_WarCry::class.java, MSG.SKILL_ARES_WARCRY_NAME, 10, 0, 3 * 1000),
        ;

        override val god: God = Ares
        override val skillId: String = name
    }

}