package org.ndk.godsimulator.wobject.entity

import net.minecraft.server.v1_12_R1.EntityLiving
import org.bukkit.util.Vector
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.minecraft.language.MSGHolder

class EntityPattern<T : EntityLiving>(
    val id: String,
    override val nameMSG: MSGHolder,
    val mob: MobPattern<T>,
    val hologramTranslation: Vector,
) : MSGNameHolder {
    override val defaultPhName: String = "pattern"
}