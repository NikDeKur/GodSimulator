@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.rpg.stat

import org.ndk.global.interfaces.Snowflake
import org.ndk.global.tools.Tools
import org.ndk.godsimulator.language.MSGNameHolder
import org.ndk.godsimulator.rpg.buff.RPGBuff
import org.ndk.minecraft.language.MSGHolder
import kotlin.reflect.KClass

abstract class RPGStat<T : Comparable<T>> : Snowflake<String>, MSGNameHolder {
    override val defaultPhName: String = "stat"
    abstract val defaultValue: T
    abstract val clazz: KClass<T>

    abstract val nameBuffMSG: MSGHolder

    abstract fun merge(value: T, other: T): T
    abstract fun deMerge(value: T, other: T): T

    /**
     * Returns the random value between min and max
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return the random value between min and max
     */
    abstract fun range(min: T, max: T): T

    abstract fun read(value: String): T?


    inline fun new(value: T = defaultValue): RPGBuff<T> {
        return RPGBuff(this, value)
    }



    abstract class Double : RPGStat<kotlin.Double>() {
        override val defaultValue: kotlin.Double = 0.0
        override val clazz: KClass<kotlin.Double> = kotlin.Double::class
        override fun merge(value: kotlin.Double, other: kotlin.Double): kotlin.Double {
            return value + other
        }
        override fun deMerge(value: kotlin.Double, other: kotlin.Double): kotlin.Double {
            return value - other
        }

        override fun range(min: kotlin.Double, max: kotlin.Double): kotlin.Double {
            return Tools.randDouble(min, max)
        }

        override fun read(value: String): kotlin.Double? {
            return value.toDoubleOrNull()
        }
    }

    abstract class Float : RPGStat<kotlin.Float>() {
        override val defaultValue: kotlin.Float = 0f
        override val clazz: KClass<kotlin.Float> = kotlin.Float::class
        override fun merge(value: kotlin.Float, other: kotlin.Float): kotlin.Float {
            return value + other
        }
        override fun deMerge(value: kotlin.Float, other: kotlin.Float): kotlin.Float {
            return value - other
        }

        override fun range(min: kotlin.Float, max: kotlin.Float): kotlin.Float {
            return Tools.randFloat(min, max)
        }

        override fun read(value: String): kotlin.Float? {
            return value.toFloatOrNull()
        }

    }

    abstract class Int : RPGStat<kotlin.Int>() {
        override val defaultValue: kotlin.Int = 0
        override val clazz: KClass<kotlin.Int> = kotlin.Int::class
        override fun merge(value: kotlin.Int, other: kotlin.Int): kotlin.Int {
            return value + other
        }
        override fun deMerge(value: kotlin.Int, other: kotlin.Int): kotlin.Int {
            return value - other
        }

        override fun range(min: kotlin.Int, max: kotlin.Int): kotlin.Int {
            return Tools.randInt(min, max)
        }

        override fun read(value: String): kotlin.Int? {
            return value.toIntOrNull()
        }
    }

    abstract class BigInteger : RPGStat<java.math.BigInteger>() {
        override val defaultValue: java.math.BigInteger = java.math.BigInteger.ZERO
        override val clazz: KClass<java.math.BigInteger> = java.math.BigInteger::class
        override fun merge(value: java.math.BigInteger, other: java.math.BigInteger): java.math.BigInteger {
            return value + other
        }
        override fun deMerge(value: java.math.BigInteger, other: java.math.BigInteger): java.math.BigInteger {
            return value - other
        }

        override fun range(min: java.math.BigInteger, max: java.math.BigInteger): java.math.BigInteger {
            return Tools.randBigInt(min, max)
        }

        override fun read(value: String): java.math.BigInteger? {
            return value.toBigIntegerOrNull()
        }
    }

    abstract class Boolean : RPGStat<kotlin.Boolean>() {
        override val defaultValue: kotlin.Boolean = false
        override val clazz: KClass<kotlin.Boolean> = kotlin.Boolean::class
        override fun merge(value: kotlin.Boolean, other: kotlin.Boolean): kotlin.Boolean {
            return value || other
        }

        override fun range(min: kotlin.Boolean, max: kotlin.Boolean): kotlin.Boolean {
            throw UnsupportedOperationException("Boolean stat can't be ranged")
        }

        override fun deMerge(value: kotlin.Boolean, other: kotlin.Boolean): kotlin.Boolean {
            return value && other
        }

        override fun read(value: String): kotlin.Boolean? {
            return Tools.parseBooleanOrNull(value)
        }
    }

    /**
     * Interface for stats that can be multiplied
     *
     * Used in [RPGProfile] registry to multiply stats
     */
    interface Multiplier : Snowflake<String>
}