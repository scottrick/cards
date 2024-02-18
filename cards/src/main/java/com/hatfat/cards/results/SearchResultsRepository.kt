package com.hatfat.cards.results

import com.hatfat.cards.data.CardsRepository
import com.hatfat.cards.data.SearchResults
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchResultsRepository @Inject constructor(
) : CardsRepository() {

    private val searchResultsMap: HashMap<UUID, SearchResults> = HashMap()

    fun addSearchResults(searchResults: SearchResults): UUID {
        val uuid = UUID.randomUUID()
        searchResultsMap[uuid] = searchResults
        return uuid
    }

    fun getSearchResults(uuid: UUID?): SearchResults? {
        return searchResultsMap[uuid]
    }

    fun removeSearchResults(uuid: UUID) {
        searchResultsMap.remove(uuid)
    }
}