package com.hatfat.cards.results.general

import com.hatfat.cards.data.SearchResults

abstract class SearchResultsDataProvider {
    abstract fun getCardDataForPosition(
        searchResults: SearchResults,
        position: Int,
        cardData: SearchResultsCardData,
    )
}

