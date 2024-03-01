package com.hatfat.trek1.results

import android.content.Context
import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.glide.CardZoomTransformation
import com.hatfat.cards.results.general.SearchResultsCardData
import com.hatfat.cards.results.general.SearchResultsDataProvider
import com.hatfat.trek1.R
import com.hatfat.trek1.repo.Trek1CardRepository
import com.hatfat.trek1.repo.Trek1SetRepository
import com.hatfat.trek1.search.Trek1SearchResults
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Trek1SearchResultsDataProvider @Inject constructor(
    private val cardRepository: Trek1CardRepository,
    private val setRepository: Trek1SetRepository,
    @ApplicationContext private val context: Context,
) : SearchResultsDataProvider() {

    // Trek1 card images do not have borders.
    private val standardZoomTransformation = CardZoomTransformation(0.275f, 0.625f)
    private val tribbleZoomTransformation = CardZoomTransformation(0.275f, 0.75f)

    override fun getCardDataForPosition(
        searchResults: SearchResults, position: Int, cardData: SearchResultsCardData
    ) {
        (searchResults as Trek1SearchResults).also {
            val cardId = it.getResult(position)
            cardRepository.cardsMap.value?.get(cardId)?.let { card ->
                val set = setRepository.getSetForCard(card)

                cardData.title = card.name
                cardData.subtitle = card.type
                cardData.listExtraText = set.abbr ?: context.getString(R.string.unknown)
                cardData.carouselInfoText1 = card.type ?: context.getString(R.string.unknown)
                cardData.carouselInfoText2 = card.info
                cardData.carouselInfoText3 = set.name
                cardData.frontImageUrl = card.frontImageUrl
                cardData.backImageUrl = if (card.hasBack) card.backImageUrl else null
                cardData.hasDifferentBack = card.hasBack
                cardData.infoList = null
                cardData.cardBackResourceId = R.drawable.cardback

                cardData.cardZoomTransformation = when (card.type?.lowercase()) {
                    "tribble" -> tribbleZoomTransformation
                    else -> standardZoomTransformation
                }
            }
        }
    }
}
