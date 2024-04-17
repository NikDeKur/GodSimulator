package org.ndk.godsimulator.selling

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.profile.PlayerProfile.Companion.profile
import org.ndk.godsimulator.world.WorldsManager.data
import org.ndk.klib.isZero
import org.ndk.klib.toBeautifulString
import org.ndk.minecraft.extension.sendLangMsg
import org.ndk.minecraft.movement.OptiPlayerMoveEvent

object SellZoneListener : Listener {

    @EventHandler
    fun onPlayerMove(event: OptiPlayerMoveEvent) {
        val player = event.player
        val to = event.to
        val world = to.world.data
        val sellZone = world.objectsManager.findSellZones(to).firstOrNull() ?: return
        sell(sellZone, player)
    }

    fun sell(sellZone: SellZone, player: Player) {
        val profile = player.profile
        val bagFill = profile.bagFill
        if (bagFill.isZero) return
        val sold = sellZone.sell(profile)
        if (sold.isZero) return
        player.sendLangMsg(
            MSG.SELL_SUCCESS,
            "amount" to bagFill.toBeautifulString(),
            "price" to sold.toBeautifulString()
        )
    }

}