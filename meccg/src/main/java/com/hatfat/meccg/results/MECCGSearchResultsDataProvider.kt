package com.hatfat.meccg.results

import android.content.Context
import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.glide.CardZoomTransformation
import com.hatfat.cards.results.general.SearchResultsCardData
import com.hatfat.cards.results.general.SearchResultsDataProvider
import com.hatfat.meccg.R
import com.hatfat.meccg.repo.MECCGCardRepository
import com.hatfat.meccg.repo.MECCGSetRepository
import com.hatfat.meccg.search.MECCGSearchResults
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MECCGSearchResultsDataProvider @Inject constructor(
    private val cardRepository: MECCGCardRepository,
    private val setRepository: MECCGSetRepository,
    @ApplicationContext private val context: Context,
) : SearchResultsDataProvider() {

    private val cardBackHelper = MECCGCardBackHelper()

    private val standardZoomTransformation = CardZoomTransformation(0.5f, 0.625f, 0.625f, 0.625f)

    override fun getCardDataForPosition(
        searchResults: SearchResults, position: Int, cardData: SearchResultsCardData
    ) {
        (searchResults as MECCGSearchResults).also { results ->
            val cardId = results.getResult(position)
            cardRepository.cardsMap.value?.get(cardId)?.let { card ->
                val set = setRepository.setMap.value?.get(card.set)?.name ?: card.set
                ?: context.getString(R.string.unknown)

                cardData.title = card.nameEN
                cardData.subtitle = card.primary
                cardData.listExtraText = card.set
                cardData.carouselInfoText1 = card.primary
                cardData.carouselInfoText2 = card.precise
                cardData.carouselInfoText3 = set
                cardData.frontImageUrl = card.imageUrl
                cardData.backImageUrl = null
                cardData.hasDifferentBack = false
                cardData.infoList = null
                cardData.cardBackResourceId = cardBackHelper.getCardBackResourceId(card)
                cardData.cardZoomTransformation = standardZoomTransformation

                cardData.cardAccentColor = when (card.alignment?.lowercase()) {
                    "neutral" -> {
                        when (card.primary?.lowercase()) {
                            "region" -> R.color.neutral_region_accent
                            else -> R.color.neutral_accent
                        }
                    }

                    "dual" -> {
                        when (card.race?.lowercase()) {
                            "animal" -> R.color.hero_accent
                            else -> R.color.minion_accent
                        }
                    }

                    "hero" -> {
                        when (card.primary?.lowercase()) {
                            "character" -> when (card.nameEN?.lowercase()) {
                                "alatar" -> R.color.alatar_accent
                                "gandalf" -> R.color.gandalf_accent
                                "pallando" -> R.color.pallando_accent
                                "radagast" -> R.color.radagast_accent
                                "saruman" -> R.color.saruman_accent
                                else -> R.color.hero_character_accent
                            }

                            "site" -> R.color.hero_site_accent
                            else -> R.color.hero_accent
                        }
                    }

                    "fallen/lord", "fallen-wizard" -> {
                        when (card.primary?.lowercase()) {
                            "character" -> when (card.nameEN?.lowercase()) {
                                "alatar" -> R.color.alatar_accent
                                "gandalf" -> R.color.gandalf_accent
                                "pallando" -> R.color.pallando_accent
                                "radagast" -> R.color.radagast_accent
                                "saruman" -> R.color.saruman_accent
                                else -> R.color.fallen_accent
                            }

                            "site" -> R.color.fallen_site_accent
                            else -> R.color.fallen_accent
                        }
                    }

                    "minion" -> {
                        when (card.primary?.lowercase()) {
                            "character" -> {
                                if (card.race?.lowercase()?.startsWith("ringwraith") == true) {
                                    R.color.evil_accent
                                } else {
                                    R.color.minion_character_accent
                                }
                            }

                            "site" -> R.color.minion_site_accent
                            else -> R.color.minion_accent
                        }
                    }

                    "balrog" -> {
                        when (card.primary?.lowercase()) {
                            "character" -> when (card.nameEN?.lowercase()) {
                                "the balrog" -> R.color.evil_accent
                                else -> R.color.balrog_character_accent
                            }

                            "site" -> R.color.balrog_site_accent
                            else -> R.color.balrog_accent
                        }
                    }

                    else -> R.color.colorOnBackground
                }

            }
        }
    }
}
