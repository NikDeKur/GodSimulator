package org.ndk.godsimulator.god

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.ndk.global.interfaces.Snowflake
import org.ndk.global.placeholders.Placeholder
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.godsimulator.profile.ProfileSkills
import org.ndk.godsimulator.skill.Skill
import org.ndk.godsimulator.skill.SkillExecution
import org.ndk.minecraft.item.ItemPattern


abstract class God: Placeholder, MSGNameHolder, Snowflake<String> {
    override val defaultPhName: String = "god"

    open val isSelectable: Boolean = true

    abstract val skills: List<Skill>

    override fun getIcon(player: Player): ItemStack {
        return ItemPattern.from(Material.BARRIER)
            .setDisplayName(nameMSG)
            .setLore(MSG.GOD_SELECT_LORE)
            .setTag("god", id)
            .build(player, getFinalPlaceholder(player))
    }

    open fun updateUnlockedSkills(skills: ProfileSkills, bind : Boolean = false) {
        val profile = skills.profile
        this.skills.forEachIndexed { index, skill ->
            if (profile.level < skill.requiredLevel) return
            if (profile.rebirth < skill.requiredRebirth) return
            skills.unlock(skill)
            if (bind)
                skills.bind(index, skill)
        }
    }
}


class NotSelectedGod : God() {
    override val id: String = "NotSelected"
    override val nameMSG: MSG = MSG.GOD_NOT_SELECTED
    override val isSelectable: Boolean = false
    override val skills: List<Skill> = emptyList()

    override fun updateUnlockedSkills(skills: ProfileSkills, bind: Boolean) {
        // Do nothing
    }
}

class Zeus : God() {
    override val id: String = "Zeus"
    override val nameMSG: MSG = MSG.GOD_NAME_ZEUS
    override val skills: List<Skill> = Skills.entries

    enum class Skills(
        override val executionClass: Class<out SkillExecution>,
        override val nameMSG: MSG,
        override val requiredLevel: Int,
        override val requiredRebirth: Int,
        override val cooldownMs: Long
    ) : Skill {
        THUNDERBOLT(Zeus_ThunderBolt::class.java, MSG.SKILL_ZEUS_THUNDERBOLT_NAME, 0, 0, 1000),
        EARTHQUAKE(Zeus_Earthquake::class.java, MSG.SKILL_ZEUS_EARTHQUAKE_NAME, 10, 0, 3 * 1000),
        ;

        override val id: String = "$ID-$name"
    }


    companion object {
        const val ID = "Zeus"
    }
}




class Apollo : God() {
    override val id: String = ID
    override val nameMSG: MSG = MSG.GOD_NAME_APOLLO
    override val skills: List<Skill> = Skills.entries

    enum class Skills(
        override val executionClass: Class<out SkillExecution>,
        override val nameMSG: MSG,
        override val requiredLevel: Int,
        override val requiredRebirth: Int,
        override val cooldownMs: Long
    ) : Skill {
        SUNBEAM(Apollo_SunBeam::class.java, MSG.SKILL_APOLLO_SUNBEAM_NAME, 0, 0, 1000),
        SOLARBLAST(Apollo_SolarBlast::class.java, MSG.SKILL_APOLLO_SOLARBLAST_NAME, 10, 0, 3 * 1000),
        ;
        override val id: String = "$ID-$name"
    }


    companion object {
        const val ID = "Apollo"
    }
}


// Боги: Посейдон (Вода), Аид (Смерть), Арес (Войны)

class Poseidon : God() {
    override val id: String = "Poseidon"
    override val nameMSG: MSG = MSG.GOD_NAME_POSEIDON
    override val skills: List<Skill> = Skills.entries

    enum class Skills(
        override val executionClass: Class<out SkillExecution>,
        override val nameMSG: MSG,
        override val requiredRebirth: Int,
        override val requiredLevel: Int,
        override val cooldownMs: Long
    ) : Skill {
        // WATERFALL(Poseidon_Waterfall::class.java, MSG.SKILL_POSEIDON_WATERFALL_NAME, 0, 0, 1000),
        // TIDALWAVE(Poseidon_TidalWave::class.java, MSG.SKILL_POSEIDON_TIDALWAVE_NAME, 10, 0, 3 * 1000),
        ;
        override val id: String = "$ID-$name"
    }


    companion object {
        const val ID = "Poseidon"
    }
}

// Аид
class Hades : God() {
    override val id: String = "Hades"
    override val nameMSG: MSG = MSG.GOD_NAME_HADES
    override val skills: List<Skill> = Skills.entries

    enum class Skills(
        override val executionClass: Class<out SkillExecution>,
        override val nameMSG: MSG,
        override val requiredLevel: Int,
        override val requiredRebirth: Int,
        override val cooldownMs: Long
    ) : Skill {
        // DEATHRAY(Hades_DeathRay::class.java, MSG.SKILL_HADES_DEATHRAY_NAME, 0, 0, 1000),
        // SOULSUCKER(Hades_SoulSucker::class.java, MSG.SKILL_HADES_SOULSUCKER_NAME, 10, 0, 3 * 1000),
        ;
        override val id: String = "$ID-$name"
    }


    companion object {
        const val ID = "Hades"
    }
}


class Ares : God() {
    override val id: String = "Ares"
    override val nameMSG: MSG = MSG.GOD_NAME_ARES
    override val skills: List<Skill> = Skills.entries

    enum class Skills(
        override val executionClass: Class<out SkillExecution>,
        override val nameMSG: MSG,
        override val requiredLevel: Int,
        override val requiredRebirth: Int,
        override val cooldownMs: Long
    ) : Skill {
        // BLOODLUST(Ares_BloodLust::class.java, MSG.SKILL_ARES_BLOODLUST_NAME, 0, 0, 1000),
        // WARCRY(Ares_WarCry::class.java, MSG.SKILL_ARES_WARCRY_NAME, 10, 0, 3 * 1000),
        ;
        override val id: String = "$ID-$name"
    }


    companion object {
        const val ID = "Ares"
    }
}