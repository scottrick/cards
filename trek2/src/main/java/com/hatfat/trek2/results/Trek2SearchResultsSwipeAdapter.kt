package com.hatfat.trek2.results

import com.hatfat.cards.results.swipe.SearchResultsSwipeAdapter
import javax.inject.Inject
import javax.inject.Named

class Trek2SearchResultsSwipeAdapter @Inject constructor(
//    private val cardRepository: MECCGCardRepository,
    @Named("should use playstore images") private val shouldUsePlayStoreImages: Boolean
) : SearchResultsSwipeAdapter(shouldUsePlayStoreImages) {

    override fun isFlippable(position: Int): Boolean {
        return false
    }

    override fun imageUrlForFront(position: Int): String {
        return "https://hips.hearstapps.com/pop.h-cdn.co/assets/16/26/4000x2000/landscape-1467144815-starshipenterprise.jpg"
//        (searchResults as MECCGSearchResults).also {
//            val cardId = it.getResult(position)
//            return cardRepository.cardsMap.value?.get(cardId)?.imageUrl ?: ""
//        }
    }

    override fun imageUrlForBack(position: Int): String {
        return imageUrlForFront(position)
    }

    override fun extraText(position: Int): String {
        return "trek2"
//        (searchResults as MECCGSearchResults).also {
//            val cardId = it.getResult(position)
//            val card = cardRepository.cardsMap.value?.get(cardId)
//            return "${card?.set} - ${card?.precise}"
//        }
    }
}