package org.ndk.godsimulator.quest.goals

import org.ndk.godsimulator.quest.goal.CounterGoal
import org.ndk.godsimulator.quest.objective.Objective
import org.ndk.godsimulator.wobject.building.BuildingPattern
import org.ndk.godsimulator.wobject.entity.EntityPattern
import org.ndk.minecraft.extension.getLangMsg
import org.ndk.minecraft.language.MSGHolder

class KillLivingObjectsGoal(
    objective: Objective,
    val progressMSG: MSGHolder,
    override val target: Int
) : CounterGoal(objective) {

    override fun getProgress(): String {
        val name = player.getLangMsg(progressMSG)
        return "$name: $progress/$target"
    }

    override fun clone(): KillLivingObjectsGoal {
        return KillLivingObjectsGoal(objective, progressMSG, target)
    }
}

class KillBuildingsGoal(
    objective: Objective,
    val pattern: BuildingPattern,
    override val target: Int
) : CounterGoal(objective) {

    override fun getProgress(): String {
        val name = pattern.getName(player)
        return "$name: $progress/$target"
    }

    override fun clone(): KillBuildingsGoal {
        return KillBuildingsGoal(objective, pattern, target)
    }
}

class KillEntitiesGoal(
    objective: Objective,
    val pattern: EntityPattern<*>,
    override val target: Int
) : CounterGoal(objective) {

    override fun getProgress(): String {
        val name = pattern.getName(player)
        return "$name: $progress/$target"
    }

    override fun clone(): KillEntitiesGoal {
        return KillEntitiesGoal(objective, pattern, target)
    }
}


class DamageLivingObjectsGoal(
    objective: Objective,
    val name: String,
    override val target: Int
) : CounterGoal(objective) {

    override fun getProgress(): String {
        return "$name: $progress/$target"
    }

    override fun clone(): DamageLivingObjectsGoal {
        return DamageLivingObjectsGoal(objective, name, target)
    }
}
