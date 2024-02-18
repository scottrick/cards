package com.hatfat.swccg.results

import android.content.Context
import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.glide.CardZoomTransformation
import com.hatfat.cards.results.general.SearchResultsCardData
import com.hatfat.cards.results.general.SearchResultsDataProvider
import com.hatfat.swccg.R
import com.hatfat.swccg.repo.SWCCGCardRepository
import com.hatfat.swccg.repo.SWCCGSetRepository
import com.hatfat.swccg.search.SWCCGSearchResults
import com.hatfat.swccg.util.SWCCGCardBackHelper
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

    private val standardZoomTransformation =
        CardZoomTransformation(0.333f, 1.0625f, 1.125f, 0.9375f)
    private val noLoreZoomTransformation = CardZoomTransformation(0.375f, 0.625f)
    private val admiralsOrderZoomTransformation = CardZoomTransformation(0.25f, 0.75f)
    private val locationTransformation = CardZoomTransformation(0.25f, 1.125f, 0.75f, 0.6875f)

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

                if (card.front.type?.startsWith("jedi test", true) == true) {
                    cardData.cardZoomTransformation = noLoreZoomTransformation
                } else {
                    cardData.cardZoomTransformation = when (card.front.type?.lowercase()) {
                        "admiral's order" -> admiralsOrderZoomTransformation
                        "epic event", "objective", "mission" -> noLoreZoomTransformation
                        "location" -> locationTransformation
                        "interrupt", "effect" -> {
                            if ((card.front.title ?: "").contains("&")) {
                                // combo cards
                                noLoreZoomTransformation
                            } else {
                                standardZoomTransformation
                            }
                        }

                        else -> standardZoomTransformation
                    }
                }
            }
        }
    }
}