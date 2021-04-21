package com.hatfat.trek1.results

import com.hatfat.cards.results.swipe.SearchResultsSwipeAdapter
import com.hatfat.trek1.repo.Trek1CardRepository
import com.hatfat.trek1.repo.Trek1SetRepository
import com.hatfat.trek1.search.Trek1SearchResults
import javax.inject.Inject
import javax.inject.Named

class Trek1SearchResultsSwipeAdapter @Inject constructor(
    private val cardRepository: Trek1CardRepository,
    private val setRepository: Trek1SetRepository,
    @Named("should use playstore images") private val shouldUsePlayStoreImages: Boolean
) : SearchResultsSwipeAdapter(shouldUsePlayStoreImages) {

    override fun isFlippable(position: Int): Boolean {
        (searchResults as Trek1SearchResults).also {
            val cardId = it.getResult(position)
            return cardRepository.cardsMap.value?.get(cardId)?.hasBack ?: false
        }
    }

    override fun imageUrlForFront(position: Int): String {
        (searchResults as Trek1SearchResults).also {
            val cardId = it.getResult(position)
            return cardRepository.cardsMap.value?.get(cardId)?.frontImageUrl ?: ""
        }
    }

    override fun imageUrlForBack(position: Int): String {
        (searchResults as Trek1SearchResults).also {
            val cardId = it.getResult(position)
            return cardRepository.cardsMap.value?.get(cardId)?.backImageUrl ?: ""
        }
    }

    override fun extraText(position: Int): String {
        (searchResults as Trek1SearchResults).also {
            val cardId = it.getResult(position)
            val card = cardRepository.cardsMap.value?.get(cardId)

            val set = setRepository.setMap.value?.get(card?.release ?: "")
            val setString = set?.name ?: "Unknown"
            return "$setString - ${card?.info}"
        }
    }
}