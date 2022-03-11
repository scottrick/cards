package com.hatfat.cards.info

import com.hatfat.cards.data.SearchResults
import java.io.Serializable

data class InfoSelection(
    val position: Int,
    val searchResults: SearchResults,
) : Serializable