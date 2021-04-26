package com.hatfat.trek2.results

import android.content.Context
import com.hatfat.cards.results.swipe.SearchResultsSwipeAdapter
import com.hatfat.trek2.repo.Trek2CardRepository
import com.hatfat.trek2.repo.Trek2SetRepository
import com.hatfat.trek2.search.Trek2SearchResults
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Named

class Trek2SearchResultsSwipeAdapter @Inject constructor(
    private val cardRepository: Trek2CardRepository,
    private val setRepository: Trek2SetRepository,
    @Named("should use playstore images") private val shouldUsePlayStoreImages: Boolean,
    @ApplicationContext context: Context
) : SearchResultsSwipeAdapter(shouldUsePlayStoreImages, context) {

    override fun isFlippable(position: Int): Boolean {
        (searchResults as Trek2SearchResults).also {
            val cardId = it.getResult(position)
            return cardRepository.cardsMap.value?.get(cardId)?.hasBack ?: false
        }
    }

    override fun imageUrlForFront(position: Int): String {
        (searchResults as Trek2SearchResults).also {
            val cardId = it.getResult(position)
            return cardRepository.cardsMap.value?.get(cardId)?.frontImageUrl ?: ""
        }
    }

    override fun imageUrlForBack(position: Int): String {
        (searchResults as Trek2SearchResults).also {
            val cardId = it.getResult(position)
            return cardRepository.cardsMap.value?.get(cardId)?.backImageUrl ?: ""
        }
    }

    override fun extraText(position: Int): String {
        (searchResults as Trek2SearchResults).also {
            val cardId = it.getResult(position)
            val card = cardRepository.cardsMap.value?.get(cardId)

            val set = setRepository.setMap.value?.get(card?.set ?: "")
            val setString = set?.name ?: "Unknown"
            return "$setString - ${card?.rarity}"
        }
    }
}