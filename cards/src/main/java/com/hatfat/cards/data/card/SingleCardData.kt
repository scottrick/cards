package com.hatfat.cards.data.card

import com.hatfat.cards.data.SearchResults
import java.io.Serializable

data class SingleCardData(
    val position: Int,
    val searchResults: SearchResults,
    val isFlipped: Boolean,
    val isRotated: Boolean,
) : Serializable