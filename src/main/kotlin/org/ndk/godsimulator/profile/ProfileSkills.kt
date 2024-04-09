package org.ndk.godsimulator.profile

import org.ndk.godsimulator.skill.Skill

class ProfileSkills(val profile: PlayerProfile) {

    val scopes = profile.scopes

    val unlockedSkills by scopes::unlockedSkills

    fun unlock(skill: Skill) {
        unlockedSkills.add(skill.id)
    }

    fun lock(skill: Skill) {
        unlockedSkills.remove(skill.id)
    }


    val bindings: MutableMap<String, Any>
        get() = profile.scopes.skillBindings

    fun bind(key: Int, binding: Skill) {
        bindings[key.toString()] = binding.id
    }

    fun unbind(key: Int) {
        bindings.remove(key.toString())
    }

    fun getBinding(key: Int): Skill? {
        val bindingStr = bindings[key.toString()] as? String ?: return null
        val binding = bindingStr.toIntOrNull() ?: return null
        return profile.god.skills[binding]
    }
}