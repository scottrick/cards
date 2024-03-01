package com.hatfat.trek2.results

import android.content.Context
import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.glide.CardZoomTransformation
import com.hatfat.cards.results.general.SearchResultsCardData
import com.hatfat.cards.results.general.SearchResultsDataProvider
import com.hatfat.trek2.R
import com.hatfat.trek2.repo.Trek2CardRepository
import com.hatfat.trek2.repo.Trek2SetRepository
import com.hatfat.trek2.search.Trek2SearchResults
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Trek2SearchResultsDataProvider @Inject constructor(
    private val cardRepository: Trek2CardRepository,
    private val setRepository: Trek2SetRepository,
    @ApplicationContext private val context: Context,
) : SearchResultsDataProvider() {

    // Trek2 card images do not have borders.
    private val standardZoomTransformation = CardZoomTransformation(0.275f, 0.625f)

    override fun getCardDataForPosition(
        searchResults: SearchResults, position: Int, cardData: SearchResultsCardData
    ) {
        (searchResults as Trek2SearchResults).also {
            val cardId = it.getResult(position)
            cardRepository.cardsMap.value?.get(cardId)?.let { card ->
                val set = setRepository.setMap.value?.get(card.set)?.name
                    ?: context.getString(R.string.unknown)

                cardData.title = card.name
                cardData.subtitle = card.type
                cardData.listExtraText = card.set
                cardData.carouselInfoText1 = card.type ?: context.getString(R.string.unknown)
                cardData.carouselInfoText2 = card.rarity
                cardData.carouselInfoText3 = set
                cardData.frontImageUrl = card.frontImageUrl
                cardData.backImageUrl = if (card.hasBack) card.backImageUrl else null
                cardData.hasDifferentBack = card.hasBack
                cardData.infoList = null
                cardData.cardBackResourceId = R.drawable.cardback

                cardData.cardZoomTransformation = when (card.type?.lowercase()) {
                    else -> standardZoomTransformation
                }
            }
        }
    }
}
