package com.hatfat.cards.search.filter.advanced

import com.hatfat.cards.R
import java.io.Serializable

enum class AdvancedFilterMode(
    val displayNameResource: Int,
) : Serializable {
    CONTAINS(R.string.advanced_filter_contains),
    DOES_NOT_CONTAIN(R.string.advanced_filter_does_not_contain),
    EQUALS(R.string.advanced_filter_equals),
    NOT_EQUALS(R.string.advanced_filter_not_equals),
    GREATER_THAN(R.string.advanced_filter_greater_than),
    GREATER_THAN_OR_EQUAL(R.string.advanced_filter_greater_than_or_equal),
    LESS_THAN(R.string.advanced_filter_less_than),
    LESS_THAN_OR_EQUAL(R.string.advanced_filter_less_than_or_equal),
}