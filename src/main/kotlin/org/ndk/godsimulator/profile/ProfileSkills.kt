package org.ndk.godsimulator.profile

import org.ndk.godsimulator.skill.Skill

class ProfileSkills(val profile: PlayerProfile) {

    val scopes = profile.scopes

    val unlockedSkills by scopes::unlockedSkills

    fun isUnlocked(skill: Skill): Boolean {
        return unlockedSkills.contains(skill.skillId)
    }

    fun unlock(skill: Skill) {
        unlockedSkills.add(skill.skillId)
    }

    fun lock(skill: Skill) {
        unlockedSkills.remove(skill.skillId)
    }


    // Actual type is <Int, String (skillId)>
    val bindings: MutableMap<String, Any>
        get() = profile.scopes.skillBindings

    val skillToBind = HashMap<String, Int>()

    fun bind(key: Int, binding: Skill) {
        bindings[key.toString()] = binding.skillId
        skillToBind[binding.skillId] = key
    }

    fun isBinded(skill: Skill): Boolean {
        return bindings.containsValue(skill.skillId)
    }

    fun unbind(key: Int) {
        val skill = bindings.remove(key.toString())
        if (skill is String) {
            skillToBind.remove(skill)
        }
    }

    fun getBinding(key: Int): Skill? {
        val skillId = bindings[key.toString()] as? String ?: return null
        return profile.god.skills[skillId]
    }

    fun getBind(skill: Skill): Int? {
        return skillToBind[skill.skillId]
    }

    init {
        bindings.forEach { (k, v) ->
            skillToBind[v.toString()] = k.toInt()
        }
    }
}