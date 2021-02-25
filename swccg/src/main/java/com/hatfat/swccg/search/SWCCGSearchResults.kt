package com.hatfat.swccg.search

import com.hatfat.cards.results.SearchResults
import com.hatfat.swccg.data.SWCCGCardIdList

class SWCCGSearchResults(
    private val swccgCardIdList: SWCCGCardIdList
) : SearchResults() {
    override val size: Int
        get() = swccgCardIdList.cardIds.size

    override fun getResult(position: Int): Int {
        return swccgCardIdList.cardIds[position]
    }
}