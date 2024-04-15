package org.ndk.godsimulator.scoreboard

import org.bukkit.entity.Player
import org.ndk.godsimulator.database.Database.accessor
import org.ndk.godsimulator.extension.getLangMsg
import org.ndk.godsimulator.language.MSG
import org.ndk.klib.toSingletonSet
import org.ndk.minecraft.scoreboard.AssembleAdapter

class ScoreboardAdapter : AssembleAdapter {

    override fun getData(player: Player): Array<String> {
        val accessor = player.accessor

        val profile = accessor.profile
        val god = profile.god

        val title = profile.getLangMsg(
            MSG.SCOREBOARD_MAIN_TITLE,
            "player" to player,
            "profile" to profile.getPlaceholder(player),
        ).text

        val lines = profile.getLangMsg(
            MSG.SCOREBOARD_MAIN_LINES, mapOf(
            "god" to god.getPlaceholder(player),
            "player" to player.toSingletonSet(),
            "profile" to profile.getPlaceholder(player),
        )).arrayText

        return arrayOf(title, *lines)
    }
}