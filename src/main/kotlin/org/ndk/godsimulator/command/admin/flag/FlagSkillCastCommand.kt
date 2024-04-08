package org.ndk.godsimulator.command.admin.flag

import org.ndk.godsimulator.profile.PlayerProfile
import kotlin.reflect.KMutableProperty1

class FlagSkillCastCommand : FlagCommand() {
    override val property: KMutableProperty1<PlayerProfile, Boolean> = PlayerProfile::passSkillCast
    override val name: String = "skillcast"
}