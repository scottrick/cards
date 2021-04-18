package com.hatfat.trek2.search

import com.hatfat.cards.data.SearchResults

class Trek2SearchResults(
    private val cardIdList: List<String>
) : SearchResults() {
    override val size: Int
        get() = cardIdList.size

    override fun getResult(position: Int): String {
        return cardIdList[position]
    }
}