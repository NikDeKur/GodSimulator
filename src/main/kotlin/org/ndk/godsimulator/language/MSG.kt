package org.ndk.godsimulator.language

import org.ndk.minecraft.language.LanguagesManager
import org.ndk.minecraft.language.MSGHolder

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

    PRICE("&fPrice:{entries}"),
    PRICE_ENTRY("&6{price} {currency.name}"),

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
        "&fCome here to sell your blocks!"
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
        "&fExp: &a{profile.xp}/{profile.xpForNextLevel}",
        "",
        "&fStamina: &a{profile.stamina}/100",
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


    GOD_SELECT_TITLE("&6Select a god!"),
    /**
     * Placeholders:
     * 1. {god}
     */
    GOD_SELECT_SUCCESS("&aYou have successfully chosen the god '{god.name}&a'!"),

    LOCATION_NAME_OVERWORLD("&aOverworld"),
    LOCATION_NAME_NETHER("&cNether"),

    PLAYER_MENU_GUI_TITLE("&6Menu"),
    PLAYER_MENU_SHOP_ITEM_DISPLAY("&6Shop"),

    SHOP_GUI_TITLE("&6Shop"),
    SHOP_NPC_BAG_NAME("&6Bags Shop"),
    SHOP_NPC_AURA_NAME("&6Auras Shop"),

    BAGS_SHOP_GUI_TITLE("&6Bags"),
    AURAS_SHOP_GUI_TITLE("&6Auras"),

    SHOP_BUY("&7[LMB] &aBuy"),
    SHOP_EQUIP("&7[RMB] &aEquip"),
    SHOP_EQUIPPED("&aAlready equipped!"),



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

    BAG_LITTLE("&6Little Bag"),
    BAG_SMALL("&eSmall Bag"),
    BAG_MEDIUM("&aMedium Bag"),
    BAG_ENHANCED("&bEnhanced Bag"),
    BAG_PREMIUM("&9Premium Bag"),
    BAG_GRAND("&dGrand Bag"),
    BAG_MASTERWORK("&5Masterwork Bag"),
    BAG_ULTIMATE("&3Ultimate Bag"),
    BAG_MYTHICAL("&dMythical Bag"),
    BAG_CELESTIAL("&bCelestial Bag"),
    BAG_LEGENDARY("&6Legendary Bag"),
    BAG_ETERNAL("&fEternal Bag"),
    BAG_ROYAL("&bRoyal Bag"),
    BAG_IMPERIAL("&cImperial Bag"),
    BAG_EXALTED("&5Exalted Bag"),
    BAG_MAJESTIC("&2Majestic Bag"),
    BAG_OMEGA("&9Omega Bag"),
    BAG_INFINITY("&dInfinity Bag"),
    BAG_AUTOSELL("&dAutoSell Bag"),

    BAG_ICON_DISPLAY("&6{bag.name}"),
    BAG_ICON_LORE(
        "",
        "&fSize: &6{bag.size}",
    ),

    SKILL_CAST_COOLDOWN("&cYou can use skill '{skill.name}&c' in &6{time} &cseconds!"),

    GOD_NOT_SELECTED_NAME("&cNot selected"),
    GOD_NOT_SELECTED_DESCRIPTION("",
        "&cYou have not selected a god yet, and probably,",
        "&cthat are seeing this message because of an error!",
        "&cIf it's, contact the administration!"
    ),

    GOD_ZEUS_NAME("&6Zeus"),
    GOD_ZEUS_DESCRIPTION("",
        "&fThe mighty Zeus, the supreme ruler of the skies and thunder",
        "&fwielding lightning bolts with unrivaled power.",
        "&fHe is the embodiment of law, order, and justice,",
        "&fpresiding over the realm of gods and mortals alike, with his",
        "&fmajestic presence casting a radiant aura across the heavens!"
    ),

    GOD_APOLLO_NAME("&eApollo"),
    GOD_APOLLO_DESCRIPTION("",
        "&fBehold Apollo, the radiant god of music, arts, and knowledge,",
        "&fwhose divine melody brings harmony to the cosmos.",
        "&fWith his golden lyre and radiant bow, he guides humanity",
        "&fthrough the realms of poetry, prophecy, and healing arts, his",
        "&brilliance illuminating the path to enlightenment and beauty!"
    ),

    GOD_POSEIDON_NAME("&bPoseidon"),
    GOD_POSEIDON_DESCRIPTION("",
        "&fPoseidon, the mighty ruler of the seas, whose trident commands",
        "&fthe ebb and flow of ocean tides. With unrivaled power over",
        "&fearthquakes and storms, he reigns over the watery abyss, and his",
        "&fpresence strikes awe and reverence in the hearts of sailors",
        "&fand land-dwellers alike, shaping the very fabric",
        "&fof the aquatic world with his divine will!"
    ),

    GOD_HADES_NAME("&8Hades"),
    GOD_HADES_DESCRIPTION("",
        "&fHades, the enigmatic lord of the underworld, where the souls",
        "&fof the departed find their eternal rest. Cloaked in shadows,",
        "&fhe rules over the realm of the dead with stern justice,",
        "&fyet offers solace to the weary souls seeking refuge in his domain.",
        "&fHis dark visage conceals profound wisdom and sovereignty over",
        "&fthe mysteries of life and death, holding sway over",
        "&fthe fate of all mortal beings!"
    ),

    GOD_ARES_NAME("&cAres"),
    GOD_ARES_DESCRIPTION("",
        "&fAres, the formidable god of war, whose ferocious presence",
        "&fignites the flames of conflict and strife. Clad in armor forged",
        "&fof divine steel, he revels in the chaos of battle, inspiring",
        "&fwarriors to acts of valor and glory. With his relentless spirit",
        "&fand unyielding determination, he embodies the raw power of warfare,",
        "&fdriving armies to victory or defeat with the thunderous roar",
        "&fof battle cries echoing across the battlefield!"
    ),

    SKILL_ZEUS_THUNDERBOLT_NAME("&6Thunderbolt"),
    SKILL_ZEUS_THUNDERBOLT_DESCRIPTION("", "&fStrike the enemy with a powerful lightning bolt in radius of &63 &fblocks!"),

    SKILL_ZEUS_EARTHQUAKE_NAME("&6Earthquake"),
    SKILL_ZEUS_EARTHQUAKE_DESCRIPTION("", "&fCause an earthquake for &62 &fseconds in radius of &65 &fblocks!"),

    SKILL_APOLLO_SUNBEAM_NAME("&eSun Beam"),
    SKILL_APOLLO_SUNBEAM_DESCRIPTION("", "&fCreate a powerful sun beam that fly &615 &fblocks through the enemies and burn them!"),

    SKILL_APOLLO_SOLARBLAST_NAME("&eSolar Blast"),
    SKILL_APOLLO_SOLARBLAST_DESCRIPTION("", "&eCause a powerful solar blast that deals damage to all enemies in radius of &65 &eblocks!"),


    BUILDING_NAME_MAYA("&6Maya Temple"),
    BUILDING_NAME_HOUSE("&aHouse"),
    BUILDING_NAME_TREE("&2Tree"),

    ENTITY_NAME_SKELETON("&6Skeleton"),

    AURA_ICON_DISPLAY("{aura.name}"),
    AURA_TEST_NAME("&7Test Aura"),

    AURA_NAME_SPARKS("&eSparks"),
    AURA_NAME_EMBERS("&cEmbers"),
    AURA_NAME_GREEN_ENERGY("&aGreen Energy"),
    AURA_NAME_MALEVOLENCE("&5Malevolence"),
    AURA_NAME_LIGHTNING("&eLightning"),
    AURA_NAME_GRAVITY("&7Gravity"),
    AURA_NAME_PURPLE_ENERGY("&5Purple Energy"),
    AURA_NAME_BLUE_ENERGY("&9Blue Energy"),
    AURA_NAME_RADIANCE("&4Radiance"),
    AURA_NAME_MELONS("&aMelons"),
    AURA_NAME_PIERCING("&3Piercing"),
    AURA_NAME_ULTRAVIOLET("&dUltraviolet"),
    AURA_NAME_VITALITY("&4Vitality"),
    AURA_NAME_DARKNESS("&8Darkness"),
    AURA_NAME_PURITY("&ePurity"),
    AURA_NAME_LUSTROUS("&2Lustrous"),
    AURA_NAME_DARK_MATTER("&0Dark Matter"),

    ITEM_NAME_IRON_SWORD("&7Iron Sword"),

    RPG_STAT_NAME_SPEED_BONUS("Speed"),
    RPG_STAT_NAME_BUFF_SPEED_BONUS("&f{buff.name} &6+{buff.value}"),

    RPG_STAT_NAME_HEALTH("Health"),
    RPG_STAT_NAME_BUFF_HEALTH("&f{buff.name}: &6+{buff.value}"),

    RPG_STAT_NAME_REGENERATION("Regeneration"),
    RPG_STAT_NAME_BUFF_REGENERATION("&f{buff.name}: &6+{buff.value}"),

    RPG_STAT_NAME_REGENERATION_MULTIPLIER("Regeneration"),
    RPG_STAT_NAME_BUFF_REGENERATION_MULTIPLIER("&f{buff.name}: &6x{buff.value}"),

    RPG_STAT_NAME_DAMAGE_MULTIPLIER("Damage"),
    RPG_STAT_NAME_BUFF_DAMAGE_MULTIPLIER("&f{buff.name}: &6x{buff.value}"),

    RPG_STAT_NAME_EXP_MULTIPLIER("Experience"),
    RPG_STAT_NAME_BUFF_EXP_MULTIPLIER("&f{buff.name}: &6x{buff.value}"),

    RPG_STAT_NAME_COINS_MULTIPLIER("Coins"),
    RPG_STAT_NAME_BUFF_COINS_MULTIPLIER("&f{buff.name}: &6x{buff.value}"),

    RPG_STAT_NAME_SOULS_MULTIPLIER("Souls"),
    RPG_STAT_NAME_BUFF_SOULS_MULTIPLIER("&f{buff.name}: &6x{buff.value}"),

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

    CURRENCY_COINS("&6Coins"),
    CURRENCY_SOULS("&bSouls"),



    //----------------------------------------
    // GLOBAL MENU START
    //----------------------------------------
    GLOBAL_MENU_TITLE("&6Menu"),
    GLOBAL_MENU_BACK_DISPLAY("&6Back"),
    GLOBAL_MENU_BACK_LORE("", "&fClick to go back to the profile menu"),


    GLOBAL_MENU_SKILLS_ITEM_DISPLAY("&6Skills"),
    GLOBAL_MENU_SKILLS_ITEM_LORE("", "&fClick to open the skills menu"),

    MENU_SKILLS_TITLE("&6Skills"),

    SKILL_REQUIRE_LEVEL("&cRequire level &6{skill.requiredLevel} &cto unlock"),
    SKILL_UNBIND("&7[RMB] &aClick to unbind from the slot '{bind}'!"),
    SKILL_BIND("&7[LMB] &aClick to bind to a slot!"),
    SKILL_BIND_CLICK_SLOT("&aClick slot &61-9 &ato bind the skill to the slot!"),
    SKILL_BIND_SUCCESS("&aYou have successfully binded skill '{skill.name}&a' to the slot &6{slot}&a!"),




    GLOBAL_MENU_PROFILE_ITEM_DISPLAY("&6Profiles"),
    GLOBAL_MENU_PROFILE_ITEM_LORE("", "&fClick to open the profiles menu"),

    PROFILES_GUI_TITLE("&6Profiles"),

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


    GLOBAL_MENU_REBIRTH_ITEM_DISPLAY("&6Rebirth"),
    GLOBAL_MENU_REBIRTH_ITEM_LORE("", "&fClick to open the rebirth menu"),
    GLOBAL_MENU_REBIRTH_TITLE("&6Rebirth"),

    GLOBAL_MENU_QUESTS_ITEM_DISPLAY("&6Quests"),
    GLOBAL_MENU_QUESTS_ITEM_LORE("", "&fClick to open the quests menu"),
    GLOBAL_MENU_QUESTS_TITLE("&6Quests"),

    QUEST_GOAL_COMPLETED("&aCompleted"),
    QUEST_GOAL_UNCOMPLETED("&7Uncompleted"),
    QUEST_GOAL_PROGRESS("&6{progress}/{target}"),



    GLOBAL_MENU_PETS_ITEM_DISPLAY("&6Pets"),
    GLOBAL_MENU_PETS_ITEM_LORE("", "&fClick to open the pets menu"),
    GLOBAL_MENU_PETS_TITLE("&6Pets"),

    PET_ICON_DISPLAY("{pet.name}"),
    PET_TEST_NAME("&7Test Pet"),

    GLOBAL_MENU_ITEMS_ITEM_DISPLAY("&6Items"),
    GLOBAL_MENU_ITEMS_ITEM_LORE("", "&fClick to open the items menu"),
    GLOBAL_MENU_ITEMS_TITLE("&6Items"),


    GLOBAL_MENU_STATISTICS_ITEM_DISPLAY("&6Statistics"),
    GLOBAL_MENU_STATISTICS_ITEM_LORE("", "&fClick to open the statistics menu"),
    GLOBAL_MENU_STATISTICS_TITLE("&6Statistics"),


    GLOBAL_MENU_SETTINGS_ITEM_DISPLAY("&6Settings"),
    GLOBAL_MENU_SETTINGS_ITEM_LORE("", "&fClick to open the settings menu"),
    GLOBAL_MENU_SETTINGS_TITLE("&6Settings"),

    RARITY_NAME_COMMON("&fCommon"),
    RARITY_NAME_UNCOMMON("&aUncommon"),
    RARITY_NAME_RARE("&9Rare"),
    RARITY_NAME_EPIC("&5Epic"),
    RARITY_NAME_MYTHIC("&dMythic"),
    RARITY_NAME_LEGENDARY("&6Legendary"),
    RARITY_NAME_SPECIAL("&eSPECIAL"),

    PET_NAME_ICE_MINOTAUR("&eIce Minotaur"),
    PET_NAME_ICE_DOLPHIN("&eIce Dolphin"),
    PET_NAME_ICE_EAGLE("&eIce Eagle"),
    PET_NAME_ICE_HYDRA("&eIce Hydra"),
    PET_NAME_DESERT_DOLPHIN("&eDesert Dolphin"),
    PET_NAME_DESERT_PEGASUS("&eDesert Pegasus"),
    PET_NAME_DESERT_CERBERUS("&eDesert Cerberus"),
    PET_NAME_DESERT_EAGLE("&eDesert Eagle"),
    PET_NAME_DESERT_HYDRA("&eDesert Hydra"),
    PET_NAME_DESERT_MINOTAUR("&eDesert Minotaur"),

    QUEST_TEST_NAME("&6Deal &c{damage} &6to anything!"),
    QUEST_TEST_DESCRIPTION("",
        "&fYou need to deal &c{damage} &fdamage to complete it!",
        "&fYou can damage anything: players, mobs, blocks, etc.",
    ),

    //----------------------------------------
    // GLOBAL MENU END
    //----------------------------------------
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
}
