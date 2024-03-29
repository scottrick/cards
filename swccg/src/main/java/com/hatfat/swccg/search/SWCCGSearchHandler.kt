package com.hatfat.swccg.search

import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.search.CardSearchHandler
import com.hatfat.cards.search.filter.SearchParams
import com.hatfat.swccg.data.SWCCGCardIdList
import com.hatfat.swccg.repo.SWCCGCardRepository
import com.hatfat.swccg.repo.SWCCGSetRepository
import com.hatfat.swccg.search.filter.SWCCGFilter
import com.hatfat.swccg.search.filter.text.SWCCGTextFilter
import com.hatfat.swccg.search.filter.text.SWCCGTextFilterMode
import javax.inject.Inject

class SWCCGSearchHandler @Inject constructor(
    private val cardRepo: SWCCGCardRepository,
    private val setRepository: SWCCGSetRepository
) : CardSearchHandler {
    override fun performSearch(searchParams: SearchParams): SearchResults {
        val filters = mutableListOf<SWCCGFilter>()

        if (searchParams.textFilters.isNotEmpty()) {
            val searchOptions = searchParams.textFilters.map { it.extra as SWCCGTextFilterMode }.toSet()
            val stringFilter = SWCCGTextFilter(searchParams.basicTextSearchString, searchOptions)
            filters.add(stringFilter)
        }

        if (searchParams.spinnerFilters.isNotEmpty()) {
            val spinnerFilters = searchParams.spinnerFilters.map { it as SWCCGFilter }
            filters.addAll(spinnerFilters)
        }

        if (searchParams.advancedfilters.isNotEmpty()) {
            val advancedFilters = searchParams.advancedfilters.map { it as SWCCGFilter }
            filters.addAll(advancedFilters)
        }

        var results = cardRepo.sortedCardIds.value?.cardIds?.toList() ?: emptyList()

        filters.forEach { filter ->
            results = results.filter { cardId ->
                cardRepo.cardsMap.value?.get(cardId)?.let { card ->
                    filter.filter(card, setRepository)
                } ?: false
            }
        }

        return SWCCGSearchResults(SWCCGCardIdList(results.toIntArray()))
    }
}