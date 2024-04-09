package org.ndk.godsimulator.skill.binding

import org.ndk.godsimulator.god.God
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.godsimulator.skill.Skill

class SkillBindings(val profile: PlayerProfile) {

    val bindings: MutableMap<String, Any>
        get() = profile.scopes.skillBindings

    fun bind(key: Int, binding: Skill) {
        bindings[key.toString()] = binding.id
    }

    fun unbind(key: Int) {
        bindings.remove(key.toString())
    }

    fun getBinding(key: Int): String? {
        return bindings[key.toString()] as? String
    }

    fun getBinding(god: God, key: Int): Skill? {
        val bindingStr = getBinding(key) ?: return null
        val binding = bindingStr.toIntOrNull() ?: return null
        return god.skills[binding]
    }
}