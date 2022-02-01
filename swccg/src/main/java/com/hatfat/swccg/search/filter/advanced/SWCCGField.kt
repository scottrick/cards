package com.hatfat.swccg.search.filter.advanced

import java.io.Serializable

enum class SWCCGField(
    val displayName: String
) : Serializable {
    ABILITY("Ability"),
    ARMOR("Armor"),
    CHARACTERISTIC("Characteristics"),
    CONCEPT_BY("Concept by"),
    DARK_SIDE_ICONS("Dark Side Icons"),
    DEPLOY("Deploy"),
    DESTINY("Destiny"),
    EXTRA_TEXT("Extra Text"),
    FEROCITY("Ferocity"),
    FORFEIT("Forfeit"),
    GAMETEXT("Gametext"),
    HYPERSPEED("Hyperspeed"),
    ICONS("Icons"),
    LANDSPEED("Landspeed"),
    LIGHT_SIDE_ICONS("Light Side Icons"),
    LORE("Lore"),
    MANEUVER("Maneuver"),
    PARSEC("Parsec"),
    POLITICS("Politics"),
    POWER("Power"),
    PRINTINGS("Printings"),
    RARITY("Rarity"),
    SET("Set"),
    SIDE("Side"),
    SUB_TYPE("Subtype"),
    TITLE("Title"),
    TYPE("Type"),
    UNIQUENESS("Uniqueness"),
}