package com.hatfat.cards.search.filter.advanced

import java.io.Serializable

enum class AdvancedFilterMode(
    val displayName: String
) : Serializable {
    CONTAINS("Contains"),
    DOES_NOT_CONTAIN("Doesn't Contain"),
    EQUALS("Equals"),
    NOT_EQUALS("Not Equals"),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL("<="),
}