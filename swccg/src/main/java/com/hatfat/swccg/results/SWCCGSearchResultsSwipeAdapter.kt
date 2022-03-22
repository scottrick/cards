package com.hatfat.swccg.results

import android.content.Context
import androidx.annotation.DrawableRes
import com.hatfat.cards.results.swipe.SearchResultsSwipeAdapter
import com.hatfat.swccg.repo.SWCCGCardRepository
import com.hatfat.swccg.repo.SWCCGSetRepository
import com.hatfat.swccg.search.SWCCGSearchResults
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Named

class SWCCGSearchResultsSwipeAdapter @Inject constructor(
    private val cardRepository: SWCCGCardRepository,
    private val setRepository: SWCCGSetRepository,
    @Named("should use playstore images") private val shouldUsePlayStoreImages: Boolean,
    @ApplicationContext context: Context
) : SearchResultsSwipeAdapter(shouldUsePlayStoreImages, context) {

    private val cardBackHelper = SWCCGCardBackHelper()

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

    override fun extraText(position: Int): String {
        (searchResults as SWCCGSearchResults).also {
            val cardId = it.getResult(position)
            val card = cardRepository.cardsMap.value?.get(cardId)
            val set = setRepository.setMap.value?.get(card?.set)?.gempName ?: "Unknown"
            return "$set - ${card?.rarity}"
        }
    }

    override fun hasRulings(position: Int): Boolean {
        (searchResults as SWCCGSearchResults).also {
            val cardId = it.getResult(position)
            val card = cardRepository.cardsMap.value?.get(cardId)
            return !card?.rulings.isNullOrEmpty()
        }
    }

    @DrawableRes
    override fun loadingImageResourceId(position: Int): Int {
        (searchResults as SWCCGSearchResults).also {
            val cardId = it.getResult(position)
            val card = cardRepository.cardsMap.value?.get(cardId)
            return cardBackHelper.getCardBackResourceId(card)
        }
    }
}
