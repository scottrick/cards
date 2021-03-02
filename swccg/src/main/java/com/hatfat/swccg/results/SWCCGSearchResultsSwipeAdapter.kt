package com.hatfat.swccg.results

import com.hatfat.cards.results.swipe.SearchResultsSwipeAdapter
import com.hatfat.swccg.repo.SWCCGCardsRepository
import com.hatfat.swccg.search.SWCCGSearchResults
import javax.inject.Inject
import javax.inject.Named

class SWCCGSearchResultsSwipeAdapter @Inject constructor(
    private val cardRepository: SWCCGCardsRepository,
    @Named("should use playstore images") private val shouldUsePlayStoreImages: Boolean
) : SearchResultsSwipeAdapter(shouldUsePlayStoreImages) {

    override fun isFlippable(position: Int): Boolean {
        (searchResults as SWCCGSearchResults).also {
            val cardId = it.getResult(position)
            return cardRepository.cardsMap.value?.get(cardId)?.back != null
        }
    }

    override fun imageUrlForFront(position: Int): String {
        (searchResults as SWCCGSearchResults).also {
            val cardId = it.getResult(position)
            return cardRepository.cardsMap.value?.get(cardId)?.front?.imageUrl ?: ""
        }
    }

    override fun imageUrlForBack(position: Int): String {
        (searchResults as SWCCGSearchResults).also {
            val cardId = it.getResult(position)
            return cardRepository.cardsMap.value?.get(cardId)?.back?.imageUrl ?: ""
        }
    }
}
