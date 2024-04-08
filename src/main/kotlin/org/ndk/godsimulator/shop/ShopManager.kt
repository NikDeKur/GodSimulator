@file:Suppress("NOTHING_TO_INLINE")

package org.ndk.godsimulator.shop


import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.util.Vector
import org.ndk.godsimulator.GodSimulator
import org.ndk.godsimulator.GodSimulator.Companion.shopManager
import org.ndk.godsimulator.equipable.impl.AurasShopGUI
import org.ndk.godsimulator.equipable.impl.BagsShopGUI
import org.ndk.godsimulator.extension.readMSG
import org.ndk.minecraft.extension.*
import org.ndk.minecraft.modules.PluginModule
import org.ndk.minecraft.plugin.ServerPlugin
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ShopManager : PluginModule {

    override val id: String = "ShopManager"

    val patterns = ConcurrentHashMap<String, Shop.Pattern>()
    val shops = ConcurrentHashMap<UUID, Shop>()
    val shopsEntitiesUUIDs = HashSet<UUID>()
    val shopGUIs = HashMap<String, Class<out ShopGUI<*>>>()
        .apply {
            put("bag", BagsShopGUI::class.java)
            put("aura", AurasShopGUI::class.java)
        }

    override fun onLoad(plugin: ServerPlugin) {
        val config = plugin.configsManager.load("shops")
        for (shop in config.getListSection()) {
            val pattern = shop.readShopPatternOrThrow("")
            patterns[pattern.id] = pattern
        }
        shopManager = this
    }

    override fun onUnload(plugin: ServerPlugin) {
        for (world in GodSimulator.worlds) {
            world.objectsManager.removeShops()
        }
        patterns.clear()
        shops.clear()
        shopsEntitiesUUIDs.clear()
    }





    fun addShop(pattern: Shop.Pattern, location: Location): Shop {
        val shop = Shop(id, pattern, location)
        shop.spawn()
        shops[shop.id] = shop
        shopsEntitiesUUIDs.add(shop.entity.uniqueId)
        shop.world.objectsManager.register(shop)
        return shop
    }

    fun isShopEntity(uuid: UUID): Boolean {
        return shopsEntitiesUUIDs.contains(uuid)
    }


    companion object {
        fun ConfigurationSection.readShopPattern(path: String, def: Shop.Pattern? = null): Shop.Pattern? {
            val section = getSection(path) ?: return def
            val id = section.name
            val msgName = section.readMSG("name") ?: return def
            val entityType = section.readEntityType("entity") ?: return def
            val hologramTranslation = section.readVector("hologramTranslation") ?: Vector(0.0, 0.0, 0.0)
            return Shop.Pattern(id, msgName, entityType, hologramTranslation)
        }
        inline fun ConfigurationSection.readShopPatternOrThrow(path: String): Shop.Pattern {
            return readShopPattern(path, null) ?: throwNotFound(path)
        }

        /**
         * Also register (and spawn) shop via shopManager
         */
        fun ConfigurationSection.readShop(path: String, world: World, def: Shop? = null): Shop? {
            val section = getSection(path) ?: return def
            val patternStr = section.getStringOrThrow("pattern")
            val pattern = shopManager.patterns[patternStr] ?: return def
            val location = section.readLocation("location", world) ?: return def
            return shopManager.addShop(pattern, location)
        }
        /**
         * Also register (and spawn) shop via shopManager
         */
        inline fun ConfigurationSection.readShopOrThrow(path: String, world: World): Shop {
            return readShop(path, world, null) ?: throwNotFound(path)
        }
    }
}