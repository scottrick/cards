package com.hatfat.meccg.search

import com.hatfat.cards.data.SearchResults

class MECCGSearchResults(
    private val cardIds: IntArray,
) : SearchResults() {
    override val size: Int
        get() = cardIds.size

    override fun getResult(position: Int): Int {
        return cardIds[position]
    }
}