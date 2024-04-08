@file:Suppress("NOTHING_TO_INLINE", "OVERRIDE_BY_INLINE", "UNCHECKED_CAST")

package org.ndk.godsimulator.rpg.buff

import org.ndk.global.map.multi.MultiHashMap
import org.ndk.godsimulator.rpg.stat.RPGStat
import java.util.*


open class BuffsListImpl : MultiHashMap<String, UUID, RPGBuff<*>>(), BuffsList {

    
    override fun <T : Comparable<T>> getBuffs(id: String): Collection<RPGBuff<T>> {
        val res = get(id) ?: return emptyList()
        return res.values as? Collection<RPGBuff<T>> ?: emptyList()
    }

    final override inline fun <T : Comparable<T>> getBuffs(type: RPGStat<T>): Collection<RPGBuff<T>> {
        return getBuffs(type.id)
    }

    override fun <T : Comparable<T>> getBuff(type: RPGStat<T>, id: UUID): RPGBuff<T>? {
        return get(type.id, id) as? RPGBuff<T>
    }

    override fun getBuffs(): Map<String, Map<UUID, RPGBuff<*>>> {
        val res = HashMap<String, Map<UUID, RPGBuff<*>>>()
        forEach { (id, _) ->
            val buffs = getBuffs<Comparable<Any>>(id)
            if (buffs.isNotEmpty()) {
                res[id] = buffs.associateBy { it.id }
            }
        }
        return res
    }


    override fun <T : Comparable<T>> getBuffValueOrNull(type: RPGStat<T>): T? {
        var value: T? = null
        val buffs = getBuffs(type)
        for (buff in buffs) {
            value = if (value == null) {
                buff.value
            } else {
                type.merge(value, buff.value)
            }
        }
        return value
    }

    final override inline fun <T : Comparable<T>> getBuffValue(type: RPGStat<T>): T {
        return getBuffValueOrNull(type) ?: type.defaultValue
    }


    override fun <T : Comparable<T>> addBuff(buff: RPGBuff<T>) {
        beforeAddBuff(buff)
        buff.beforeAdd(this)

        put(buff.stat.id, buff.id, buff)

        buff.afterAdd(this)
        afterAddBuff(buff)

    }

    final override inline fun <T : Comparable<T>> addBuff(type: RPGStat<T>, value: T) {
        addBuff(type.new(value))
    }


    
    override fun <T : Comparable<T>> removeBuffs(type: RPGStat<T>): Collection<RPGBuff<T>> {
        val buffs = getBuffs(type)
        buffs.forEach(this::removeBuff)
        return buffs
    }

    override fun <T  : Comparable<T>> removeBuff(buff: RPGBuff<T>) {
        if (!contains(buff.stat.id, buff.id)) return

        beforeRemoveBuff(buff)
        buff.beforeRemove(this)

        remove(buff.stat.id, buff.id)

        afterRemoveBuff(buff)
        buff.afterRemove(this)
    }

    override fun forEachStat(action: (String) -> Unit) {
        keys.forEach(action)
    }

    override fun forEachBuff(action: (RPGBuff<*>) -> Unit) {
        forEach { _, map ->
            map.forEach { (_, buff) ->
                action(buff)
            }
        }
    }

    override fun clear() {
        forEach { _, map ->
            map.forEach { (_, buff) ->
                buff.afterRemove(this)
            }
        }
        super.clear()
    }
}


class ImaginaryBuffsListImpl : MultiHashMap<String, UUID, RPGBuff<*>>(), ImaginaryBuffsList {

    override fun <T : Comparable<T>> getBuffs(id: String): Collection<RPGBuff<T>> {
        val res = get(id) ?: return emptyList()
        return res.values as? Collection<RPGBuff<T>> ?: emptyList()
    }

    override inline fun <T : Comparable<T>> getBuffs(type: RPGStat<T>): Collection<RPGBuff<T>> {
        return getBuffs(type.id)
    }

    override inline fun getBuffs(): Map<String, Map<UUID, RPGBuff<*>>> = this


    override fun <T : Comparable<T>> getBuff(type: RPGStat<T>, id: UUID): RPGBuff<T>? {
        return get(type.id, id) as? RPGBuff<T>
    }

    override fun <T : Comparable<T>> getBuffValueOrNull(type: RPGStat<T>): T? {
        var value: T? = null
        val buffs = getBuffs(type)
        for (buff in buffs) {
            value = if (value == null) {
                buff.value
            } else {
                type.merge(value, buff.value)
            }
        }
        return value
    }

    override fun <T : Comparable<T>> getBuffValue(type: RPGStat<T>): T {
        return getBuffValueOrNull(type) ?: type.defaultValue
    }

    override fun <T : Comparable<T>> addBuff(buff: RPGBuff<T>) {
        put(buff.stat.id, buff.id, buff)
    }

    override fun <T : Comparable<T>> addBuff(type: RPGStat<T>, value: T) {
        addBuff(type.new(value))
    }

    override fun <T : Comparable<T>> removeBuffs(type: RPGStat<T>): Collection<RPGBuff<T>> {
        val remove = remove(type.id)?.values ?: return emptyList()
        return remove as? Collection<RPGBuff<T>> ?: emptyList()
    }

    override fun <T : Comparable<T>> removeBuff(buff: RPGBuff<T>) {
        remove(buff.stat.id, buff.id)
    }


    override fun addTo(buffsList: BuffsList): List<RPGBuff<*>> {
        val added = ArrayList<RPGBuff<*>>()
        forEachBuff {
            val buff = it.copy()
            buffsList.addBuff(buff)
            added.add(buff)
        }
        return added
    }

    override fun forEachStat(action: (String) -> Unit) {
        keys.forEach(action)
    }

    override fun forEachBuff(action: (RPGBuff<*>) -> Unit) {
        for (value in values) {
            for (value2 in value.values) {
                action(value2)
            }
        }
    }
}




open class AttachableBuffsListImpl : BuffsListImpl(), AttachableBuffsList {
    override val attaches: MutableMap<String, AttachedBuffsList> = HashMap()

    override fun attach(id: String, buffs: ImaginaryBuffsList) {
        val added = buffs.addTo(this)
        val attached = AttachedBuffsList(id, added)
        attaches[id] = attached
    }

    override fun detach(id: String) {
        val attached = attaches.remove(id) ?: return
        attached.buffs.forEach {
            removeBuff(it)
        }
    }
}

object EmptyBuffsList : BuffsList, MutableMap<String, MutableMap<UUID, RPGBuff<*>>> by HashMap(0) {
    override fun <T : Comparable<T>> getBuffs(id: String): Collection<RPGBuff<T>> = emptyList()
    override fun <T : Comparable<T>> getBuffs(type: RPGStat<T>): Collection<RPGBuff<T>> = emptyList()
    override fun getBuffs(): Map<String, Map<UUID, RPGBuff<*>>> = emptyMap()
    override fun <T : Comparable<T>> getBuff(type: RPGStat<T>, id: UUID): RPGBuff<T>? = null
    override fun <T : Comparable<T>> getBuffValueOrNull(type: RPGStat<T>): T? = null
    override fun <T : Comparable<T>> getBuffValue(type: RPGStat<T>): T = type.defaultValue
    override fun <T : Comparable<T>> addBuff(buff: RPGBuff<T>) {}
    override fun <T : Comparable<T>> addBuff(type: RPGStat<T>, value: T) {}
    override fun <T : Comparable<T>> removeBuffs(type: RPGStat<T>): Collection<RPGBuff<T>> = emptyList()
    override fun <T : Comparable<T>> removeBuff(buff: RPGBuff<T>) {}
    override fun clear() {}
    override fun forEachStat(action: (String) -> Unit) {}
    override fun forEachBuff(action: (RPGBuff<*>) -> Unit) {}
}

object EmptyImaginaryBuffsList : BuffsList by EmptyBuffsList, ImaginaryBuffsList {
    override fun addTo(buffsList: BuffsList) = emptyList<RPGBuff<*>>()
}

