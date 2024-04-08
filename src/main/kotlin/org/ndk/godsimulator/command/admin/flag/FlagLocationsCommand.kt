package org.ndk.godsimulator.command.admin.flag

import org.ndk.godsimulator.profile.PlayerProfile
import kotlin.reflect.KMutableProperty1

class FlagLocationsCommand : FlagCommand() {
    override val name: String = "passlocations"
    override val property: KMutableProperty1<PlayerProfile, Boolean> = PlayerProfile::passLocations
}