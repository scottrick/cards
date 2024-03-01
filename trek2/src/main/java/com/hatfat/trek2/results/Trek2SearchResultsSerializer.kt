package com.hatfat.trek2.results

import com.google.gson.Gson
import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.results.SearchResultsSerializer
import com.hatfat.trek2.search.Trek2SearchResults
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Trek2SearchResultsSerializer @Inject constructor(
    private val gson: Gson,
) : SearchResultsSerializer() {
    override fun serializeSearchResults(searchResults: SearchResults): String {
        return gson.toJson(searchResults)
    }

    override fun deserializeSearchResults(json: String?): SearchResults? {
        return gson.fromJson(json, Trek2SearchResults::class.java)
    }
}
