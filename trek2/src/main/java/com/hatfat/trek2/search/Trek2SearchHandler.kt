package com.hatfat.trek2.search

import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.search.CardSearchHandler
import com.hatfat.cards.search.filter.SearchParams
import javax.inject.Inject

class Trek2SearchHandler @Inject constructor(
//    private val cardRepo: MECCGCardRepository,
//    private val setRepo: MECCGSetRepository
) : CardSearchHandler {
    override fun performSearch(searchParams: SearchParams): SearchResults {
//        val filters = mutableListOf<Trek2Filter>()
//
//        if (searchParams.textFilters.isNotEmpty()) {
//            val searchOptions = searchParams.textFilters.map { it.extra as Trek2TextFilterMode }.toSet()
//            val stringFilter = Trek2TextFilter(searchParams.basicTextSearchString, searchOptions)
//            filters.add(stringFilter)
//        }

//        if (searchParams.spinnerFilters.isNotEmpty()) {
//            val spinnerFilters = searchParams.spinnerFilters.map { it as Trek2Filter }
//            filters.addAll(spinnerFilters)
//        }

//        if (searchParams.advancedfilters.isNotEmpty()) {
//            val advancedFilters = searchParams.advancedfilters.map { it as Trek2Filter }
//            filters.addAll(advancedFilters)
//        }

//        var results = cardRepo.sortedCardIds.value?.toList() ?: emptyList()
//
//        filters.forEach { filter ->
//            results = results.filter { cardId ->
//                cardRepo.cardsMap.value?.get(cardId)?.let { card ->
//                    filter.filter(card, setRepo)
//                } ?: false
//            }
//        }

        return Trek2SearchResults(emptyList())
    }
}