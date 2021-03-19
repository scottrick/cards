package com.hatfat.cards.search

import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.search.filter.SearchParams

interface CardSearchHandler {
    fun performSearch(searchParams: SearchParams): SearchResults
}