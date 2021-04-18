package com.hatfat.trek1.search

import com.hatfat.cards.data.SearchResults

class Trek1SearchResults(
    private val cardIdList: List<String>
) : SearchResults() {
    override val size: Int
        get() = cardIdList.size

    override fun getResult(position: Int): String {
        return cardIdList[position]
    }
}