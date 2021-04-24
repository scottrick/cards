package com.hatfat.trek1.search.filter.advanced

import java.io.Serializable

enum class Trek1Field(
    val displayName: String
) : Serializable {
    NAME("Name"),
    SET("Set"),
    PROPERTY("Property"),
    UNIQUENESS("Uniqueness"),
    TYPE("Type"),
    MISSION_TYPE("Mission Type"),
    DILEMMA_TYPE("Dilemma Type"),
    AFFILIATION("Affiliation"),
    CLASS("Class"),
    INTEGRITY("Integrity"),
    CUNNING("Cunning"),
    STRENGTH("Strength"),
    RANGE("Range"),
    WEAPONS("Weapons"),
    SHIELDS("Shields"),
    POINTS("Points"),
    REGION("Region"),
    QUADRANT("Quadrant"),
    SPAN("Span"),
    ICONS("Icons"),
    STAFF("Staff"),
    CHARACTERISTICS("Characteristics"),
    REQUIRES("Requires"),
    PERSONA("Persona"),
    COMMAND("Command"),
    LORE("Lore"),
    REPORTS("Reports"),
    NAMES("Names"),
    GAMETEXT("Gametext"),
    INFO_RARITY("Info/Rarity")
}