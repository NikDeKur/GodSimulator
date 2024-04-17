package org.ndk.godsimulator.economy

import org.ndk.godsimulator.economy.wallet.Wallet
import org.ndk.godsimulator.equipable.impl.Aura
import org.ndk.godsimulator.equipable.impl.AuraType
import org.ndk.godsimulator.equipable.impl.Pet
import org.ndk.godsimulator.equipable.impl.PetType
import org.ndk.godsimulator.event.equipable.EquipablePreBoughtEvent
import org.ndk.godsimulator.language.Quick
import org.ndk.godsimulator.profile.PlayerProfile
import org.ndk.minecraft.plugin.ServerPlugin.Companion.callEvent

/**
 * Interface representing a buyable item
 *
 * Interface representing not runtime objects like [Pet], [Aura], Interface represents object types, example [PetType], [AuraType]
 */
interface Buyable {

    val price: Wallet
    val buyable: Boolean


    /**
     * Called before economy the item
     *
     * Called after checking if the player has enough money [not taken yet] and if the item is buyable
     *
     * Example: check if the player already has the item
     *
     * Example: check if the player has enough space in the inventory
     *
     * @param profile the player profile
     * @param silent if the player should be notified about the purchase
     * @return true if the purchase should be continued
     */
    fun beforeBuy(profile: PlayerProfile, silent: Boolean): Boolean { return true }

    /**
     * Called after the Bukkit Event is done
     *
     * Called after [beforeBuy] and before [afterBuy]
     *
     * Example: give an item to the player
     *
     * @param profile the player profile
     * @param silent if the player should be notified about the purchase
     */
    fun afterEvent(profile: PlayerProfile, silent: Boolean): Boolean { return true }

    /**
     * Called after economy the item
     *
     * Called after the money is taken. Final step of the purchase
     *
     * Example: send a success message to the player
     *
     * @param profile the player profile
     * @param silent if the player should be notified about the purchase
     */
    fun afterBuy(profile: PlayerProfile, silent: Boolean) {}



    fun buy(profile: PlayerProfile, silent: Boolean = false): Boolean {
        val wallet = profile.wallet
        if (!buyable) return false

        // Check if the player has enough money
        val player = profile.onlinePlayer
        if (!wallet.has(price)) {
            if (!silent && player != null)
                Quick.cannotAfford(player, price, wallet)
            return false
        }

        if (!beforeBuy(profile, silent)) return false

        val event = EquipablePreBoughtEvent(profile, this, silent)
        callEvent(event)
        if (event.isCancelled) return false

        if (!afterEvent(profile, silent)) return false

        // Check if the player still has enough money and take the money
        if (!wallet.take(price)) {
            if (!silent && player != null)
                Quick.cannotAfford(player, price, wallet)
            return false
        }

        afterBuy(profile, silent)
        return true
    }
}