package com.hatfat.cards.results

import com.hatfat.cards.data.SearchResults

abstract class SearchResultsSerializer {
    abstract fun serializeSearchResults(searchResults: SearchResults): String
    abstract fun deserializeSearchResults(json: String?): SearchResults?
}