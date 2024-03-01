package com.hatfat.trek1.results

import com.google.gson.Gson
import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.results.SearchResultsSerializer
import com.hatfat.trek1.search.Trek1SearchResults
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Trek1SearchResultsSerializer @Inject constructor(
    private val gson: Gson,
) : SearchResultsSerializer() {
    override fun serializeSearchResults(searchResults: SearchResults): String {
        return gson.toJson(searchResults)
    }

    override fun deserializeSearchResults(json: String?): SearchResults? {
        return gson.fromJson(json, Trek1SearchResults::class.java)
    }
}
