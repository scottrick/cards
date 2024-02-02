package com.hatfat.meccg.results

import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.results.general.SearchResultsCardData
import com.hatfat.cards.results.general.SearchResultsDataProvider
import com.hatfat.meccg.repo.MECCGCardRepository
import com.hatfat.meccg.search.MECCGSearchResults
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MECCGSearchResultsDataProvider @Inject constructor(
    private val cardRepository: MECCGCardRepository,
) : SearchResultsDataProvider() {

    private val cardBackHelper = MECCGCardBackHelper()

    override fun getCardDataForPosition(
        searchResults: SearchResults, position: Int, cardData: SearchResultsCardData
    ) {
        (searchResults as MECCGSearchResults).also {
            val cardId = it.getResult(position)
            cardRepository.cardsMap.value?.get(cardId)?.let { card ->
                /* Dreamcards don't have rarity, so adjust the string accordingly */
                val carouselExtraText = if (card.dreamcard == true) {
                    "${card.set}"
                } else {
                    "${card.set} - ${card.precise}"
                }

                cardData.title = card.nameEN
                cardData.subtitle = card.primary
                cardData.listExtraTopText = card.precise
                cardData.listExtraBottomText = card.set
                cardData.carouselExtraText = carouselExtraText
                cardData.frontImageUrl = card.imageUrl
                cardData.backImageUrl = null
                cardData.hasDifferentBack = false
                cardData.infoList = null
                cardData.cardBackResourceId = cardBackHelper.getCardBackResourceId(card)
            }
        }
    }
}
