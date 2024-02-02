package com.hatfat.cards.results.general

import com.hatfat.cards.data.SearchResults
import java.io.Serializable

data class SearchResultsSingleCardState(
    val position: Int,
    val searchResults: SearchResults,
    val isFlipped: Boolean,
    val isRotated: Boolean,
) : Serializable
