package com.hatfat.cards.search

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.cards.search.filter.TextFilter

interface CardSearchOptionsProvider {
    fun getTextSearchOptions(): List<TextFilter>

    fun getDropdownFilterOptions(): List<SpinnerFilter>
}