package com.hatfat.cards.search.filter

data class SearchParams(
    val basicTextSearchString: String,
    val textFilters: List<TextFilter>,
    val spinnerFilters: List<SpinnerFilter>
)