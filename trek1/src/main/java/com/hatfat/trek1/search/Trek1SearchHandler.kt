package com.hatfat.trek1.search

import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.search.CardSearchHandler
import com.hatfat.cards.search.filter.SearchParams
import com.hatfat.trek1.repo.Trek1CardRepository
import com.hatfat.trek1.repo.Trek1SetRepository
import com.hatfat.trek1.search.filter.Trek1Filter
import com.hatfat.trek1.search.filter.text.Trek1TextFilter
import com.hatfat.trek1.search.filter.text.Trek1TextFilterMode
import javax.inject.Inject

class Trek1SearchHandler @Inject constructor(
    private val cardRepo: Trek1CardRepository,
    private val setRepo: Trek1SetRepository
) : CardSearchHandler {
    override fun performSearch(searchParams: SearchParams): SearchResults {
        val filters = mutableListOf<Trek1Filter>()

        if (searchParams.textFilters.isNotEmpty()) {
            val searchOptions = searchParams.textFilters.map { it.extra as Trek1TextFilterMode }.toSet()
            val stringFilter = Trek1TextFilter(searchParams.basicTextSearchString, searchOptions)
            filters.add(stringFilter)
        }

        if (searchParams.spinnerFilters.isNotEmpty()) {
            val spinnerFilters = searchParams.spinnerFilters.map { it as Trek1Filter }
            filters.addAll(spinnerFilters)
        }

        if (searchParams.advancedfilters.isNotEmpty()) {
            val advancedFilters = searchParams.advancedfilters.map { it as Trek1Filter }
            filters.addAll(advancedFilters)
        }

        var results = cardRepo.sortedCardIds.value?.toList() ?: emptyList()

        filters.forEach { filter ->
            results = results.filter { cardId ->
                cardRepo.cardsMap.value?.get(cardId)?.let { card ->
                    filter.filter(card, setRepo)
                } ?: false
            }
        }

        return Trek1SearchResults(results)
    }
}