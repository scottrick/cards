package com.hatfat.meccg.results

import com.hatfat.cards.results.swipe.SearchResultsSwipeAdapter
import com.hatfat.meccg.repo.MECCGCardRepository
import com.hatfat.meccg.search.MECCGSearchResults
import javax.inject.Inject
import javax.inject.Named

class MECCGSearchResultsSwipeAdapter @Inject constructor(
    private val cardRepository: MECCGCardRepository,
    @Named("should use playstore images") private val shouldUsePlayStoreImages: Boolean
) : SearchResultsSwipeAdapter(shouldUsePlayStoreImages) {

    override fun isFlippable(position: Int): Boolean {
        return false
    }

    override fun imageUrlForFront(position: Int): String {
        (searchResults as MECCGSearchResults).also {
            val cardId = it.getResult(position)
            return cardRepository.cardsMap.value?.get(cardId)?.imageUrl ?: ""
        }
    }

    override fun imageUrlForBack(position: Int): String {
        return imageUrlForFront(position)
    }

    override fun extraText(position: Int): String {
        (searchResults as MECCGSearchResults).also {
            val cardId = it.getResult(position)
            val card = cardRepository.cardsMap.value?.get(cardId)
            return "${card?.set} - ${card?.precise}"
        }
    }
}