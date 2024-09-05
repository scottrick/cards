package com.hatfat.meccg.search.filter.released

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.meccg.data.MECCGCard
import com.hatfat.meccg.repo.MECCGSetRepository
import com.hatfat.meccg.search.filter.MECCGFilter
import java.io.Serializable

class MECCGReleasedFilter(
    options: List<MECCGReleasedOption>,
    notSelectedOption: MECCGReleasedOption?,
    defaultOption: MECCGReleasedOption?
) : SpinnerFilter(
    options,
    notSelectedOption,
    defaultOption,
), MECCGFilter, Serializable {
    override fun filter(card: MECCGCard, setRepository: MECCGSetRepository): Boolean {
        return when ((selectedOption as MECCGReleasedOption).mode) {
            MECCGReleasedOptionMode.ALL_CARDS -> true
            MECCGReleasedOptionMode.ONLY_RELEASED -> card.released == true
            MECCGReleasedOptionMode.ONLY_UNRELEASED -> card.released == false
        }
    }
}