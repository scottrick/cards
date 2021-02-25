package com.hatfat.cards.search

import com.hatfat.cards.results.SearchResults
import com.hatfat.cards.search.filter.BasicTextSearchFilter

interface CardSearchHandler {
    fun performSearch(basicTextFilters: List<BasicTextSearchFilter>): SearchResults
}