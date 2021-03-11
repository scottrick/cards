package com.hatfat.swccg.search.filter

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.swccg.data.SWCCGCard
import java.io.Serializable

class SWCCGSideFilter(
    options: List<String>,
    notSelectedOption: String?,
    selectedOption: String? = notSelectedOption
) : SpinnerFilter(
    options,
    notSelectedOption,
    selectedOption
), SWCCGFilter, Serializable {
    override fun filter(card: SWCCGCard): Boolean {
        return selectedOption == card.side
    }
}