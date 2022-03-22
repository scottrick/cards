package com.hatfat.meccg.results

import android.content.Context
import com.hatfat.cards.results.swipe.SearchResultsSwipeAdapter
import com.hatfat.meccg.repo.MECCGCardRepository
import com.hatfat.meccg.search.MECCGSearchResults
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Named

class MECCGSearchResultsSwipeAdapter @Inject constructor(
    private val cardRepository: MECCGCardRepository,
    @Named("should use playstore images") private val shouldUsePlayStoreImages: Boolean,
    @ApplicationContext context: Context
) : SearchResultsSwipeAdapter(shouldUsePlayStoreImages, context) {

    private val cardBackHelper = MECCGCardBackHelper()

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
            val card = cardRepository.cardsMap.value?.get(cardId) ?: return ""

            /* Dreamcards don't have rarity, so adjust the string accordingly */
            return if (card.dreamcard == true) {
                "${card.set}"
            } else {
                "${card.set} - ${card.precise}"
            }
        }
    }

    override fun hasRulings(position: Int): Boolean {
        return false
    }

    override fun loadingImageResourceId(position: Int): Int {
        (searchResults as MECCGSearchResults).also {
            val cardId = it.getResult(position)
            val card = cardRepository.cardsMap.value?.get(cardId)
            return cardBackHelper.getCardBackResourceId(card)
        }
    }
}