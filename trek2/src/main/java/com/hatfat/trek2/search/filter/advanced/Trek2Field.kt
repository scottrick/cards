package com.hatfat.trek2.search.filter.advanced

import java.io.Serializable

enum class Trek2Field(
    val displayName: String
) : Serializable {
    NAME("Name"),
    SET("Set"),
    RARITY("Rarity"),
    UNIQUE("Unique"),
    COLLECTORS_INFO("Collector's Info"),
    TYPE("Type"),
    COST("Cost"),
    MISSION_TYPE("Mission Type"),
    DILEMMA_TYPE("Dilemma Type"),
    SPAN("Span"),
    POINTS("Points"),
    QUADRANT("Quadrant"),
    AFFILIATION("Affiliation"),
    ICONS("Icons"),
    STAFF("Staff"),
    KEYWORDS("Keywords"),
    CLASS("Class"),
    SPECIES("Species"),
    SKILLS("Skills"),
    INTEGRITY("Integrity"),
    CUNNING("Cunning"),
    STRENGTH("Strength"),
    RANGE("Range"),
    WEAPONS("Weapons"),
    SHIELDS("Shields"),
    TEXT("Text")
}