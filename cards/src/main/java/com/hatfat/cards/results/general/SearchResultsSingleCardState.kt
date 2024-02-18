package com.hatfat.cards.results.general

import java.io.Serializable
import java.util.UUID

data class SearchResultsSingleCardState(
    val position: Int,
    val searchResultsKey: UUID,
    val isFlipped: Boolean,
    val isRotated: Boolean,
) : Serializable
