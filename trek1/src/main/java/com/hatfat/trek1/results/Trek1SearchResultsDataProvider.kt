package com.hatfat.trek1.results

import android.content.Context
import com.hatfat.cards.data.SearchResults
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

    override fun getCardDataForPosition(
        searchResults: SearchResults, position: Int, cardData: SearchResultsCardData
    ) {
        (searchResults as Trek1SearchResults).also {
            val cardId = it.getResult(position)
            cardRepository.cardsMap.value?.get(cardId)?.let { card ->
                val set = setRepository.getSetForCard(card)
                val carouselExtraText = "${set.name}- ${card.info}"

                cardData.title = card.name
                cardData.subtitle = card.type
                cardData.listExtraTopText = card.info
                cardData.listExtraBottomText = set.abbr ?: context.getString(R.string.unknown)
                cardData.carouselExtraText = carouselExtraText
                cardData.frontImageUrl = card.frontImageUrl
                cardData.backImageUrl = if (card.hasBack) card.backImageUrl else null
                cardData.hasDifferentBack = card.hasBack
                cardData.infoList = null
                cardData.cardBackResourceId = R.drawable.cardback
            }
        }
    }
}
