package org.ndk.godsimulator.language

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profile
import org.ndk.minecraft.language.Language
import org.ndk.minecraft.language.LanguageProvider

class SimulatorLangProvider : LanguageProvider {

    override fun getLanguage(sender: CommandSender): Language {
        val player = sender as? Player ?: return GodSimulator.instance.languagesManager.defaultLang
        return player.profile.language

    }
    override fun setLanguage(sender: CommandSender, language: Language) {
        val player = sender as? Player ?: return
        player.profile.language = language
    }
}