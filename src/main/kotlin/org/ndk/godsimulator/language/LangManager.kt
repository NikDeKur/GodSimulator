package org.ndk.godsimulator.language

import org.ndk.minecraft.language.LanguagesManager

object LangManager : LanguagesManager() {

    const val PREFIX = "&7> &r"
    override val prefix: String = PREFIX

    init {
        for (entry in MSG.entries) {
            addMessage(entry, entry.defaultText)
        }
    }

}