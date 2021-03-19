package com.hatfat.swccg.search.filter.side

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.search.filter.SWCCGFilter
import java.io.Serializable

class SWCCGSideFilter(
    options: List<SWCCGSideOption>,
    notSelectedOption: SWCCGSideOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), SWCCGFilter, Serializable {
    override fun filter(card: SWCCGCard): Boolean {
        return (selectedOption as SWCCGSideOption).name == card.side
    }
}