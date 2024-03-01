package com.hatfat.meccg.results

import com.google.gson.Gson
import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.results.SearchResultsSerializer
import com.hatfat.meccg.search.MECCGSearchResults
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MECCGSearchResultsSerializer @Inject constructor(
    private val gson: Gson,
) : SearchResultsSerializer() {
    override fun serializeSearchResults(searchResults: SearchResults): String {
        return gson.toJson(searchResults)
    }

    override fun deserializeSearchResults(json: String?): SearchResults? {
        return gson.fromJson(json, MECCGSearchResults::class.java)
    }
}
