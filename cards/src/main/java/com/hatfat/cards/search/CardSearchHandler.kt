package com.hatfat.cards.search

import com.hatfat.cards.results.SearchResults
import com.hatfat.cards.search.param.SearchParams

interface CardSearchHandler {
    fun performSearch(searchParams: SearchParams): SearchResults
}