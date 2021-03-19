package com.hatfat.swccg.search.filter.type

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.search.filter.SWCCGFilter
import java.io.Serializable

class SWCCGTypeFilter(
    options: List<SWCCGTypeOption>,
    notSelectedOption: SWCCGTypeOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), SWCCGFilter, Serializable {
    override fun filter(card: SWCCGCard): Boolean {
        return card.front.type?.contains((selectedOption as SWCCGTypeOption).type) ?: false
    }
}