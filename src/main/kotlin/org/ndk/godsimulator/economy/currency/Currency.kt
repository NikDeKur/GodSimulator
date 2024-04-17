package org.ndk.godsimulator.economy.currency

import org.ndk.global.interfaces.Snowflake
import org.ndk.godsimulator.language.MSG
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.minecraft.language.MSGHolder

interface Currency : MSGNameHolder, Snowflake<String> {

    override val defaultPhName: String
        get() = "currency"



    companion object {
        val COINS = object : Currency {
            override val nameMSG: MSGHolder = MSG.CURRENCY_COINS
            override val id = "coins"
        }
        val SOULS = object : Currency {
            override val nameMSG: MSGHolder = MSG.CURRENCY_SOULS
            override val id = "souls"
        }
    }
}
