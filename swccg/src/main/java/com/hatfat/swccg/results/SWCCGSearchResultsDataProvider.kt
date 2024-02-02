package com.hatfat.swccg.results

import android.content.Context
import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.results.general.SearchResultsCardData
import com.hatfat.cards.results.general.SearchResultsDataProvider
import com.hatfat.swccg.R
import com.hatfat.swccg.repo.SWCCGCardRepository
import com.hatfat.swccg.repo.SWCCGSetRepository
import com.hatfat.swccg.search.SWCCGSearchResults
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SWCCGSearchResultsDataProvider @Inject constructor(
    private val cardRepository: SWCCGCardRepository,
    private val setRepository: SWCCGSetRepository,
    @ApplicationContext private val context: Context,
) : SearchResultsDataProvider() {

    private val cardBackHelper = SWCCGCardBackHelper()

    override fun getCardDataForPosition(
        searchResults: SearchResults, position: Int, cardData: SearchResultsCardData
    ) {
        (searchResults as SWCCGSearchResults).also {
            val cardId = it.getResult(position)
            cardRepository.cardsMap.value?.get(cardId)?.let { card ->
                val set = setRepository.setMap.value?.get(card.set)?.gempName
                    ?: context.getString(R.string.unknown)
                val carouselExtraText = "$set - ${card.rarity}"

                cardData.title = card.front.title
                cardData.subtitle = card.front.type
                cardData.listExtraTopText = card.rarity
                cardData.listExtraBottomText = setRepository.setMap.value?.get(card.set)?.abbr
                cardData.carouselExtraText = carouselExtraText
                cardData.frontImageUrl = card.front.imageUrl
                cardData.backImageUrl = card.back?.imageUrl
                cardData.hasDifferentBack = card.back != null
                cardData.infoList = card.rulings
                cardData.cardBackResourceId = cardBackHelper.getCardBackResourceId(card)
            }
        }
    }
}