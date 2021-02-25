package com.hatfat.cards.search.param

data class SearchParams(
    val basicTextSearchString: String,
    val basicTextSearchParams: List<BasicTextSearchParam>
)