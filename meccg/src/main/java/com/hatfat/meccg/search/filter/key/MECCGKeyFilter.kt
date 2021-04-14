package com.hatfat.meccg.search.filter.key

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.meccg.data.MECCGCard
import com.hatfat.meccg.repo.MECCGSetRepository
import com.hatfat.meccg.search.filter.MECCGFilter
import java.io.Serializable

class MECCGKeyFilter(
    options: List<MECCGKeyOption>,
    notSelectedOption: MECCGKeyOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), MECCGFilter, Serializable {
    override fun filter(card: MECCGCard, setRepository: MECCGSetRepository): Boolean {
        return card.race?.contains((selectedOption as MECCGKeyOption).name) == true
    }
}