package org.ndk.godsimulator.command.admin.flag

import org.ndk.godsimulator.profile.PlayerProfile
import kotlin.reflect.KMutableProperty1

class FlagAdventureCommand : FlagCommand() {
    override val property: KMutableProperty1<PlayerProfile, Boolean> = PlayerProfile::passAdventure
    override val name: String = "passadventure"
}