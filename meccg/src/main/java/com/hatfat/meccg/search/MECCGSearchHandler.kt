package com.hatfat.meccg.search

import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.search.CardSearchHandler
import com.hatfat.cards.search.filter.SearchParams
import com.hatfat.meccg.repo.MECCGCardsRepository
import com.hatfat.meccg.search.filter.MECCGFilter
import com.hatfat.meccg.search.filter.text.MECCGTextFilter
import com.hatfat.meccg.search.filter.text.MECCGTextFilterMode
import javax.inject.Inject

class MECCGSearchHandler @Inject constructor(
    private val cardRepo: MECCGCardsRepository
//        private val setRepository: SWCCGSetRepository
) : CardSearchHandler {
    override fun performSearch(searchParams: SearchParams): SearchResults {
        val filters = mutableListOf<MECCGFilter>()

        if (searchParams.textFilters.isNotEmpty()) {
            val searchOptions = searchParams.textFilters.map { it.extra as MECCGTextFilterMode }.toSet()
            val stringFilter = MECCGTextFilter(searchParams.basicTextSearchString, searchOptions)
            filters.add(stringFilter)
        }

        if (searchParams.spinnerFilters.isNotEmpty()) {
            val spinnerFilters = searchParams.spinnerFilters.map { it as MECCGFilter }
            filters.addAll(spinnerFilters)
        }

//        if (searchParams.advancedfilters.isNotEmpty()) {
//            val advancedFilters = searchParams.advancedfilters.map { it as SWCCGFilter }
//            filters.addAll(advancedFilters)
//        }

        var results = cardRepo.sortedCardIds.value?.toList() ?: emptyList()

        filters.forEach { filter ->
            results = results.filter { cardId ->
                cardRepo.cardsMap.value?.get(cardId)?.let { card ->
                    filter.filter(card)
                } ?: false
            }
        }

        return MECCGSearchResults(results)
    }
}
