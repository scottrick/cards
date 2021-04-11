package com.hatfat.meccg.search.filter.alignment

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.meccg.data.MECCGCard
import com.hatfat.meccg.search.filter.MECCGFilter
import java.io.Serializable

class MECCGAlignmentFilter(
    options: List<MECCGAlignmentOption>,
    notSelectedOption: MECCGAlignmentOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), MECCGFilter, Serializable {
    override fun filter(card: MECCGCard): Boolean {
        return (selectedOption as MECCGAlignmentOption).name == card.alignment
    }
}
