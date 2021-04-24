package com.hatfat.trek2.search

import com.hatfat.cards.data.SearchResults

class Trek2SearchResults(
    private val cardIdList: List<Int>
) : SearchResults() {
    override val size: Int
        get() = cardIdList.size

    override fun getResult(position: Int): Int {
        return cardIdList[position]
    }
}