package com.hatfat.meccg.search

import com.hatfat.cards.data.SearchResults

class MECCGSearchResults(
    private val cardIdList: List<String>
) : SearchResults() {
    override val size: Int
        get() = cardIdList.size

    override fun getResult(position: Int): String {
        return cardIdList[position]
    }
}
