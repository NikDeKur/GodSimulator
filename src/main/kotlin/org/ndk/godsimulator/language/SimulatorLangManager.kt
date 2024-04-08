package org.ndk.godsimulator.language

import org.ndk.minecraft.language.LanguagesManager

class SimulatorLangManager : LanguagesManager() {

    override val prefix: String = PREFIX

    init {
        for (entry in MSG.entries) {
            addMessage(entry, entry.defaultText)
        }
    }

    companion object {
        const val PREFIX = "&7> &r"
    }
}