package org.ndk.godsimulator.command.admin.flag

import org.ndk.godsimulator.profile.PlayerProfile
import kotlin.reflect.KMutableProperty1

class FlagCooldownCommand : FlagCommand() {
    override val property: KMutableProperty1<PlayerProfile, Boolean> = PlayerProfile::passCooldown
    override val name: String = "passcooldown"
}