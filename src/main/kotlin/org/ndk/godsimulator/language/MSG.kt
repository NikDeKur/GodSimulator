package org.ndk.godsimulator.language

import org.ndk.godsimulator.GodSimulator
import org.ndk.minecraft.language.LanguagesManager
import org.ndk.minecraft.language.MSGHolder


val MSG_FROM_ID by lazy {
    mutableMapOf<String, MSG>()
}

enum class MSG(val defaultText: String) : MSGHolder {

     // DECIMAL FORMATS
    COORDINATES_FORMAT("#,##0.#"),
    LTFIVE_SECONDS_FORMAT("#.#"),
    GTFIVE_SECONDS_FORMAT("#"),

    // BASE
    NOT_ENOUGH_PERMISSIONS("&cYou do not have enough permissions!"),
    /**
     * Placeholders:
     * 1. {number}
     */
    INCORRECT_NUMBER("&cThe number '{number}&c' is not recognized!"),
    /**
     * Placeholders:
     * 1. {page}
     */
    INCORRECT_PAGE("&cPage '{page}&c' not found!"),
    /**
     * Placeholders:
     * 1. {player}
     */
    UNKNOWN_PLAYER("&cPlayer '{name}&c' not found!"),
    UNKNOWN_PLAYER_NO_NAME("&cPlayer not found!"),
    ONLY_FOR_PLAYERS("&cThis command is only available for players!"),
    ONLY_FOR_PLAYERS_SYNTAX("&cThis syntax is only available for players!"),
    /**
     * Placeholders:
     * 1. {reason}
     */
    INCORRECT_REASON("&cThe reason '{reason}&c' is not correct!"),
    /**
     * Placeholders:
     * 1. {string}
     */
    HAS_BANNED_SYMBOLS("&cThe string '{string}&c' contains forbidden symbols!"),
    /**
     * Placeholders:
     * 1. {seconds}
     */
    COOLDOWN("&cWait &6{seconds} &cseconds before using this!"),
    /**
     * Placeholders:
     * 1. {seconds}
     */
    COOLDOWN_ON_COMMAND("&cWait &6{seconds} &cseconds before executing this command!"),
    /**
     * Placeholders:
     * 1. {type}
     */
    UNKNOWN_DATATYPE("&cData type '{type}&c' not found!"),
    ERROR_FINDING_ITEM("&cItem not found! If this is an error, please notify the administration."),
    /**
     * Placeholders:
     * 1. {price}
     * 2. {balance}
     */
    NOT_ENOUGH_MONEY("&cYou do not have enough money! You need {price}, but you have {balance}!"),

    UNKNOWN_LANGUAGE_CODE_FORMAT("&cUnknown language code format '{code}'! Example of correct format: 'en_us' or 'ru_ru'!"),
    UNKNOWN_LANGUAGE("&cLanguage with '{code}' not found!"),

    CANNOT_AFFORD("&cYou cannot afford this!{entries}"),
    CANNOT_AFFORD_ENTRY("  &fYou need &c{price} &f{currency.name}&f, but you have &e{balance}"),

    /**
     * Placeholders:
     * 1. {time}
     */
    COMMAND_ERROR("&cAn unknown error occurred while executing the command. Contact the administration! {time}"),
    /**
     * Placeholders:
     * 1. {time}
     */
    COMMAND_TAB_ERROR("&cAn unknown error occurred while tab-completing the command. Contact the administration! {time}"),
    /**
     * Placeholders:
     * 1. {time}
     * 1. {comment}
     */
    INTERNAL_ERROR("&cAn internal error occurred! Contact the administration! [{time}] {comment}"),
    NO_WE_SELECTION("&cSelect an area using WorldEdit!"),

    CONFIRM_ITEM_DISPLAY("&aConfirm!"),
    CONFIRM_ITEM_LORE("", "&fClick to confirm!"),

    CANCEL_ITEM_DISPLAY("&cCancel!"),
    CANCEL_ITEM_LORE("", "&fClick to cancel!"),

    ARROW_NEXT("&6Next page"),
    ARROW_PREV("&6Previous page"),

    // COMMON WORDS
    ENABLE("Enable"),
    DISABLE("Disable"),
    W_TRUE("True"),
    W_FALSE("False"),
    W_YES("Yes"),
    W_NO("No"),
    TIME("Time"),
    AMOUNT("Amount"),
    NAME("Name"),
    ADMIN("Admin"),
    ADMINISTRATOR("Administrator"),
    LEVEL("Level"),
    HEALTH("Health"),
    DAMAGE("Damage"),
    EXPERIENCE("Experience"),



    DEFAULT_BACK_SUCCESS("&aYou have successfully returned back!"),
    DEFAULT_BACK_DISPLAY("&6Back"),
    DEFAULT_BACK_LORE("\n&fClick to go back."),

    // GUI SORTING
    SORT_ITEM_DISPLAY("&6Sorting"),
    REVERSE_ITEM_DISPLAY("&6Sorting - Reverse"),
    /**
     * Placeholders:
     * 1. {sort}
     */
    SORT_ITEM_LORE("\n{sort.options}"),
    SORT_ITEM_NOT_SELECTED("&7"),
    SORT_ITEM_SELECTED("&a"),

    // RPG
    /**
     * Placeholders:
     * 1. {level}
     */
    LEVEL_UP("&fCongratulations, you have reached level &a{level}&f!"),

    // Gamemodes
    CMD_GAMEMODE_USAGE("&cUsage: /gm <0/1/2/3> [player]"),
    CMD_GAMEMODE_INCORRECT("&cGame mode '{mode}&c' not found!"),
    CMD_GAMEMODE_CHANGED("&aYour game mode has been set to '{gamemode.name}&a'"),
    CMD_GAMEMODE_SUCCESS("&aYou have successfully changed the game mode to '{gamemode.name}&a'"),

    GAMEMODE_SURVIVAL("survival"),
    GAMEMODE_CREATIVE("creative"),
    GAMEMODE_ADVENTURE("adventure"),
    GAMEMODE_SPECTATOR("spectator"),


    // Warps
    CMD_WARP_USAGE("&cUsage: /warp <name> [player]"),
    CMD_WARP_UNKNOWN("&cWarp '{id}&c' not found!"),
    CMD_WARP_TELEPORTED("&aYou have been teleported to warp '{warp.id}'"),
    CMD_WARP_SUCCESS("&aYou have successfully teleported player '{player.name}' to warp '{warp.id}'"),
    CMD_WARPS_USAGE("&cUsage: /warps <create/delete/list>"),
    CMD_WARPS_CREATE_USAGE("&cUsage: /warps create <name>"),
    CMD_WARPS_CREATE_ALREADY_EXISTS("&cWarp '{warp.id}' already exists!"),
    CMD_WARPS_CREATE_SUCCESS("&aYou have successfully created warp '{warp.id}'"),
    CMD_WARPS_DELETE_USAGE("&cUsage: /warps delete <name>"),
    CMD_WARPS_DELETE_SUCCESS("&aYou have successfully deleted warp '{warp.id}'"),
    CMD_WARPS_LIST_EMPTY("&cNo warps found!"),

    // TpPos
    CMD_TPPOS_SUCCESS("&aYou have successfully teleported to x={location.x} y={location.y} z={location.z} yaw={location.yaw} pitch={location.pitch}"),
    CMD_TPPOS_USAGE("&cUsage: /tppos <x> <y> <z> [yaw] [pitch]"),

    // TpHere
    CMD_TPHERE_USAGE("&cUsage: /tphere <player>"),
    CMD_TPHERE_SUCCESS("&aYou have successfully teleported player '{player.name}' to yourself"),
    CMD_TPHERE_OFFLINE_SUCCESS("&aYou have successfully teleported &noffline&r&a player '{player.name}' to yourself"),

    // Server
    CMD_SERVER_STATS(
        "&a--------- Information ---------",
        "&aUptime: &6{uptime}",
        "&aCPU: &6{cpu}",
        "&aRAM: &6{ram.used}/{ram.max}"
    ),

    // Language Command
    CMD_LANGUAGE_USAGE("&cUsage: /language <language>"),
    CMD_LANGUAGE_SUCCESS("&aLanguage successfully changed to '{lang.code.code}'"),



    CMD_ADMIN_BASE_USAGE("&cUsage: /admin <subcommand>"),

    CMD_ADMIN_DATA_USAGE("&cUsage: /admin data <save/load/set/remove/clear>"),

    CMD_ADMIN_FLAG_USAGE("&cUsage: /admin flag <flag_name> [true/false]"),
    CMD_ADMIN_FLAG_CHANGED("&aFlag '{flag}' successfully changed to '{state}'"),
    CMD_SPECIFIC_FLAG_USAGE("&cUsage: /admin flag {flag.name} [true/false]"),

    CMD_ADMIN_DATA_SAVE_SUCCESS("&aAll data has been successfully saved! Time spent: {time} ms"),
    CMD_ADMIN_DATA_SAVE_PLAYER_SUCCESS("&aPlayer '{player.name}' data has been successfully saved! Time spent: {time} ms"),

    CMD_ADMIN_DATA_LOAD_SUCCESS("&aAll data has been successfully loaded! Time spent: {time} ms"),
    CMD_ADMIN_DATA_LOAD_PLAYER_SUCCESS("&aPlayer '{player.name}' data has been successfully loaded! Time spent: {time} ms"),

    CMD_ADMIN_DATA_GET_SUCCESS("&aData for player '{player.name}' on path '{path}':"),
    CMD_ADMIN_DATA_GET_USAGE("&cUsage: /admin data get <player> [path]"),

    CMD_ADMIN_DATA_SET_SUCCESS("&aData for player '{player.name}' on path '{path}' has been successfully set to '{value}' ({type})!"),
    CMD_ADMIN_DATA_SET_TYPE_MISMATCH("&cType mismatch! Current type: '{current}', new type: '{new}'! Please, choose the same type or remove the data first."),
    CMD_ADMIN_DATA_SET_USAGE("&cUsage: /admin data set <player> <path> <value> [datatype]"),

    CMD_ADMIN_DATA_REMOVE_SUCCESS("&aData for player '{player.name}' on path '{path}' has been successfully removed!"),
    CMD_ADMIN_DATA_REMOVE_USAGE("&cUsage: /admin data remove <player> <path>"),

    CMD_ADMIN_DATA_CLEAR_SUCCESS("&aData for player '{player.name}' has been successfully cleared!"),
    CMD_ADMIN_DATA_CLEAR_USAGE("&cUsage: /admin data clear <player>"),

    CMD_ADMIN_STICK_SUCCESS("&aYou have got the admin stick!"),
    ADMIN_STICK_DISPLAY("&cAdmin Stick"),
    ADMIN_STICK_MODE_SELECTED(" &a>"),
    ADMIN_STICK_MODE_NOT_SELECTED(" &c>"),

    CMD_ADMIN_HUB_USAGE("&cUsage: /admin hub [player]"),
    CMD_ADMIN_HUB_SUCCESS_SELF("&aYou have successfully teleported to the hub!"),
    CMD_ADMIN_HUB_SUCCESS_OTHER("&aYou have successfully teleported player '{player.name}' to the hub!"),

    CMD_SAVEBUILDING_USAGE("&cUsage: /savebuilding <name>"),
    CMD_SAVEBUILDING_SUCCESS("&aSchematic successfully saved! Time spent: {time} ms"),
    CMD_SAVEBUILDING_NOT_LOOKING_EAST("&cYou must be looking to the east!"),

    CMD_SPAWNBUILDING_USAGE("&cUsage: /spawnbuilding <name> [cardinal direction]"),
    CMD_SPAWNBUILDING_SUCCESS("&aSchematic successfully spawned! Region: {region.minimumPoint} - {region.maximumPoint}"),

    SELL_SUCCESS("&aYou have successfully sold {amount} blocks for {price}!"),

    SELL_ZONE_MAIN_NAME("&a&lSell Zone"),
    SELL_ZONE_HOLOGRAM_TEXT(
        "{zone.name}",
        "&6Multiplier: &e&lx{zone.multiplier}"
    ),
    BUILDING_HOLOGRAM_TEXT(
        "&c{building.health}❤"
    ),
    ENTITY_HOLOGRAM_TEXT(
        "&c{entity.health}❤"
    ),

    SCOREBOARD_MAIN_TITLE("&6God Simulator"),

    /**
     * Placeholders:
     * 1. {player}
     * 1. {profile}
     */
    SCOREBOARD_MAIN_LINES(
        "&f                         ",
        "&fProfile: {profile.name}",
        "&fGod: {god.name}",
        "&fBalance: &a{profile.wallet.coins}",
        "&fSouls: &a{profile.wallet.souls}",
        "&fBag: &a{profile.bagFill}/{profile.bag.type.size}",
        "&fLevel: &a{profile.level}",
        "&fExp: &a{profile.xp}/{profile.xpForNextLevel}"
    ),


    BAG_FULL("&cBag is full!", "Sell items or upgrade your bag!"),

    /**
     * Placeholders:
     * 1. {bag}
     */
    BAG_BUY_SUCCESS("&aYou have successfully bought a bag '{bag.name}&a'!"),
    BAG_BUY_ALREADY_HAVE("&cYou cannot buy the bag you already have!"),
    BAG_BUY_ALREADY_HAS_INFINITY("&cYou already have an infinity bag, you cannot buy simple bags!"),
    BAG_BUY_ALREADY_HAS_AUTOSELL("&cYou already have an auto-sell bag, you cannot buy other bags!"),

    AURA_BUY_SUCCESS("&aYou have successfully bought an aura '{aura.name}&a'!"),
    AURA_BUY_ALREADY_HAVE("&cYou cannot buy the aura you already have!"),

    /**
     * Placeholders:
     * 1. {location}
     * 1. {profile}
     */
    LOCATION_UNLOCK_SUCCESS("&aYou have successfully unlocked the location '{location.name}&a'!"),

    /**
     * Placeholders:
     * 1. {location}
     */
    LOCATION_ALREADY_UNLOCKED("&cYou have already unlocked this location!"),


    LOCATION_UNLOCK_MENU_TITLE("&6Are you sure you want to unlock the location?"),

    /**
     * Placeholders:
     * 1. {location}
     */
    LOCATION_UNLOCK_ITEM_DISPLAY("&6{location.name}"),
    /**
     * Placeholders:
     * 1. {location}
     */
    LOCATION_UNLOCK_ITEM_LORE(
        "",
        "&ePrice: &a{location.price}"
    ),


    GOD_SELECT_TITLE("&6Select a god"),
    /**
     * Placeholders:
     * 1. {god}
     */
    GOD_SELECT_SUCCESS("&aYou have successfully chosen the god '{god.name}&a'!"),
    GOD_SELECT_LORE("", "&7Click to choose"),

    LOCATION_NAME_OVERWORLD("&aOverworld"),
    LOCATION_NAME_NETHER("&cNether"),

    PLAYER_MENU_GUI_TITLE("&6Menu"),
    PLAYER_MENU_SHOP_ITEM_DISPLAY("&6Shop"),

    SHOP_GUI_TITLE("&6Shop"),
    SHOP_NPC_NAME("&6Shop"),
    SHOP_BAG_TITLE("&6Shop -> Bags"),
    SHOP_AURA_TITLE("&6Shop -> Auras"),

    SHOP_BUY("&7[LMB] &aBuy"),
    SHOP_EQUIP("&7[RMB] &aEquip"),
    SHOP_EQUIPPED("&aAlready equipped!"),

    SHOP_BACK_DISPLAY("&6Back"),
    SHOP_BACK_LORE("", "&fClick to go back to shop"),

    PROFILES_GUI_TITLE("&6Profiles"),

    PROFILE_RANDOM_NAME_1("&dStar"),
    PROFILE_RANDOM_NAME_2("&6Phoenix"),
    PROFILE_RANDOM_NAME_3("&aNature"),
    PROFILE_RANDOM_NAME_4("&bSky"),
    PROFILE_RANDOM_NAME_5("&aApple"),
    PROFILE_RANDOM_NAME_6("&dSakura"),
    PROFILE_RANDOM_NAME_7("&fCloud"),
    PROFILE_RANDOM_NAME_8("&7Moon"),
    PROFILE_RANDOM_NAME_9("&cRuby"),
    PROFILE_RANDOM_NAME_10("&eBeach"),

    PROFILE_ICON_DISPLAY("{profile.name}"),
    PROFILE_ICON_LORE(
        "",
        "&eGod: {god.name}",
        "&eBalance: &a{profile.wallet.coins}",
        "&eSouls: &a{profile.wallet.souls}",
        "&eLevel: &a{profile.level}",
        "&eExp: &a{profile.xp}/{profile.xpForNextLevel}",
        "&eCreated: &a{profile.created}"
    ),
    PROFILE_ICON_SELECTED("&6You currently have this profile selected!"),
    PROFILE_ICON_NOT_SELECTED("&aClick to select!"),

    PROFILE_NOT_FOUND("&cProfile not found!"),

    PROFILE_GUI_CONFIRM_TITLE("&6Are you sure want to delete the profile?"),

    PROFILE_SELECTED("&aYou have successfully selected the profile '{profile.name}&a'!"),
    PROFILE_CREATED("&aYou have successfully created the new profile '{profile.name}&a'!"),
    PROFILE_DELETED("&aYou have successfully deleted the profile '{profile.name}&a'!"),

    PROFILE_CREATE_ICON_DISPLAY("&6Add profile"),
    PROFILE_CREATE_ICON_LORE("", "&fClick to create a new profile!"),
    PROFILE_CREATE_ICON_REQUIRE_DONATE_LORE("", "&cYou need donate '{donate}' to unlock this feature!"),

    PROFILE_SELECT_COOLDOWN("&cYou can change the profile in &6{time} &cseconds!"),

    BAG_1("&aStarter Bag"),
    BAG_2("&aSmall Bag"),
    BAG_3("&aMedium Bag"),
    BAG_4("&aBig Bag"),
    BAG_INFINITY("&dInfinity Bag"),
    BAG_AUTOSELL("&dAutoSell Bag"),

    BAG_ICON_DISPLAY("&6{bag.name}"),
    BAG_ICON_LORE(
        "",
        "&eSize: &a{bag.size}",
        "&ePrice: &a{bag.price}"
    ),

    SKILL_CAST_COOLDOWN("&cYou can use skill '{skill.name}&c' in &6{time} &cseconds!"),
    SKILL_REQUIRE_LEVEL("&cYou need to have level &6{skill.requiredLevel} &cto this skill!"),

    GOD_NOT_SELECTED("&cNot selected"),

    GOD_NAME_ZEUS("&6Zeus"),
    GOD_NAME_APOLLO("&eApollo"),
    GOD_NAME_POSEIDON("&bPoseidon"),
    GOD_NAME_HADES("&8Hades"),
    GOD_NAME_ARES("&cAres"),

    SKILL_ZEUS_THUNDERBOLT_NAME("&6Thunderbolt"),
    SKILL_ZEUS_EARTHQUAKE_NAME("&6Earthquake"),

    SKILL_APOLLO_SUNBEAM_NAME("&eSun Beam"),
    SKILL_APOLLO_SOLARBLAST_NAME("&eSolar Blast"),


    BUILDING_NAME_MAYA("&6Maya Temple"),
    BUILDING_NAME_HOUSE("&aHouse"),
    BUILDING_NAME_TREE("&2Tree"),

    ENTITY_NAME_SKELETON("&6Skeleton"),


    PET_GUI_TITLE("&6Pets"),
    PET_ICON_DISPLAY("{pet.name}"),
    PET_TEST_NAME("&7Test Pet"),

    AURA_GUI_TITLE("&6Auras"),
    AURA_ICON_DISPLAY("{aura.name}"),
    AURA_TEST_NAME("&7Test Aura"),

    AURA_NAME_SPEED("&6Speed Aura"),
    AURA_NAME_HEALTH("&cHealth Aura"),

    RPG_STAT_NAME_SPEED_BONUS("Speed"),
    RPG_STAT_NAME_BUFF_SPEED_BONUS("&7{buff.name} &6+{buff.value}"),

    RPG_STAT_NAME_HEALTH("Health"),
    RPG_STAT_NAME_BUFF_HEALTH("&7{buff.name}: &6+{buff.value}"),

    RPG_STAT_NAME_HEALTH_PROCENT("Health"),
    RPG_STAT_NAME_BUFF_HEALTH_PROCENT("&7{buff.name}: &6+{buff.value}%"),

    RPG_STAT_NAME_REGENERATION("Regeneration"),
    RPG_STAT_NAME_BUFF_REGENERATION("&7{buff.name}: &6+{buff.value}"),

    RPG_STAT_NAME_REGENERATION_PROCENT("Regeneration"),
    RPG_STAT_NAME_BUFF_REGENERATION_PROCENT("&7{buff.name}: &6+{buff.value}%"),

    RPG_STAT_NAME_DAMAGE_PROCENT("Damage"),
    RPG_STAT_NAME_BUFF_DAMAGE_PROCENT("&7{buff.name}: &6+{buff.value}%"),

    RPG_STAT_NAME_EXP_PROCENT("Experience"),
    RPG_STAT_NAME_BUFF_EXP_PROCENT("&7{buff.name}: &6+{buff.value}%"),

    RPG_STAT_NAME_BAG_FILL_PROCENT("Blocks"),
    RPG_STAT_NAME_BUFF_BAG_FILL_PROCENT("&7{buff.name}: &6+{buff.value}%"),

    REBIRTH_ONCE_SUCCESS("&aYou have successfully rebirthed!"),
    REBIRTH_SEVERAL_TIMES_SUCCESS("&aYou have successfully rebirthed {rebirth.given} times!"),
    REBIRTH_REQUIRE_LEVEL("&cYou need to have level &6{rebirth.requiredLevel} &cto rebirth!"),

    REBIRTH_GUI_TITLE("&6Rebirth"),
    REBIRTH_ICON_DISPLAY("&6Rebirth"),
    REBIRTH_ICON_LORE(
        "",
        "&eYou would lose all your progress and start from the beginning!",
        "",
        "&eRequired Level: &a{rebirth.requiredLevel}",
        "",
        "Experience Bonus: &a+{rebirth.expBonus}%",
        "Damage Bonus: &a+{rebirth.damageBonus}%",
        "Blocks Bonus: &a+{rebirth.bagFillBonus}%"
    ),

    QUEST_BOOK_TITLE("&6Quests"),

    CURRENCY_COINS("&6Coins"),
    CURRENCY_SOULS("&6Souls"),

    ;

    constructor(vararg lines: String) : this(lines.joinToString("\n"))

    /**
     * Stands for [MSGHolder.id]
     *
     * Uses to identify the message in [LanguagesManager].
     */
    override val id: String = name


    override fun toString(): String {
        return "msg{id=$id, default=$defaultText}"
    }

    init {
        MSG_FROM_ID[id] = this
    }

    companion object {

        /**
         * Gets the MSG (enum) object by its id.
         *
         * @param id The id of the message.
         * @return The MSG object or null if not found.
         */
        fun msgFromId(id: String): MSG? {
            return MSG_FROM_ID[id]
        }

        /**
         * Gets the MSGHolder object by its id.
         *
         * This method search the message by id in the languages manager.
         *
         * It will be found even if the message was registered by another plugin.
         *
         * @param id The id of the message.
         * @return The MSGHolder object or null if not found.
         */
        fun fromId(id: String): MSGHolder? {
            return GodSimulator.languagesManager.getMessage(id)
        }
    }
}
