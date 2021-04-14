package com.hatfat.meccg.search.filter.advanced

import java.io.Serializable

enum class MECCGField(
    val displayName: String
) : Serializable {
    SET("Set"),
    PRIMARY("Primary Type"),
    ALIGNMENT("Alignment"),
    ARTIST("Artist"),
    RARITY("Rarity"),
    TEXT("Text"),
    SKILL("Skill"),
    MP("Marshalling Points"),
    MIND("Mind"),
    DIRECT("Direct Influence"),
    GENERAL("General Influence"),
    PROWESS("Prowess"),
    BODY("Body"),
//    CORRUPTION("Corruption"),
    HOME("Home"),
    SECONDARY("Secondary Type"),
    RACE("Race"),
    SITE("Site"),
    PATH("Path"),
    REGION("Region"),
    HAVEN("Nearest Haven"),
    STAGE("Stage"),
    STRIKES("Strikes"),
    TITLE("Title")
}