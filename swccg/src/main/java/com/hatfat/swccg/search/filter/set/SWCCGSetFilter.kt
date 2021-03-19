package com.hatfat.swccg.search.filter.set

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.search.filter.SWCCGFilter
import java.io.Serializable

class SWCCGSetFilter(
    options: List<SWCCGSetOption>,
    notSelectedOption: SWCCGSetOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), SWCCGFilter, Serializable {
    override fun filter(card: SWCCGCard): Boolean {
        return card.printings?.map { it.set }?.contains((selectedOption as SWCCGSetOption).id) ?: false
    }
}