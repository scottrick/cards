package com.hatfat.swccg.search

import com.hatfat.cards.results.SearchResults
import com.hatfat.cards.search.CardSearchHandler
import com.hatfat.cards.search.filter.BasicTextSearchFilter
import com.hatfat.cards.temp.FakeSearchResults
import javax.inject.Inject

class SWCCGSearchHandler @Inject constructor() : CardSearchHandler {
    override fun performSearch(basicTextFilters: List<BasicTextSearchFilter>): SearchResults {
        return FakeSearchResults(listOf(49, 50, 51, 52))
    }
}