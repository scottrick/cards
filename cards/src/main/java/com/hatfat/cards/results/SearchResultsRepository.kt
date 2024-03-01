package com.hatfat.cards.results

import android.content.Context
import com.hatfat.cards.data.CardsRepository
import com.hatfat.cards.data.SearchResults
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchResultsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val searchResultsSerializer: SearchResultsSerializer,
) : CardsRepository() {

    // Memory Cache
    private val searchResultsMap: HashMap<String, SearchResults> = HashMap()

    // Disk Cache
    private val resultsPreferences =
        context.getSharedPreferences("SearchResultsRepository", Context.MODE_PRIVATE)

    fun addSearchResults(searchResults: SearchResults): UUID {
        val uuid = UUID.randomUUID()

        // Add to memory cache
        searchResultsMap[uuid.toString()] = searchResults

        // Add to SharedPreferences
        val editor = resultsPreferences.edit()
        editor.putString(
            uuid.toString(),
            searchResultsSerializer.serializeSearchResults(searchResults)
        )
        editor.apply()

        return uuid
    }

    // Load the search results into the memory cache and return them.
    fun loadSearchResults(uuid: UUID?): SearchResults? {
        uuid?.let {
            if (searchResultsMap.containsKey(uuid.toString())) {
                return searchResultsMap[uuid.toString()]
            }

            val jsonString = resultsPreferences.getString(uuid.toString(), null)
            searchResultsSerializer.deserializeSearchResults(jsonString)?.let {
                // Re-add to memory cache
                searchResultsMap[uuid.toString()] = it
                return it
            }
        }

        return null
    }

    // Removes any search results that are not in the memory cache from SharedPreferences.
    fun cleanUpOldSearchResults() {
        // Remove unloaded results from SharedPreferences
        val editor = resultsPreferences.edit()
        for (key in resultsPreferences.all.keys) {
            if (!searchResultsMap.containsKey(key)) {
                // Not loaded, so it is unused and can be removed
                editor.remove(key)
            }
        }

        editor.apply()
    }
}