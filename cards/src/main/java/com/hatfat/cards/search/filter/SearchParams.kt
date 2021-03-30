package com.hatfat.cards.search.filter

import com.hatfat.cards.search.filter.advanced.AdvancedFilter

data class SearchParams(
    val basicTextSearchString: String,
    val textFilters: List<TextFilter>,
    val spinnerFilters: List<SpinnerFilter>,
    val advancedfilters: List<AdvancedFilter>
)