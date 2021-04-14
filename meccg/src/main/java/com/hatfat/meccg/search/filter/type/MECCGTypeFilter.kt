package com.hatfat.meccg.search.filter.type

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.meccg.data.MECCGCard
import com.hatfat.meccg.repo.MECCGSetRepository
import com.hatfat.meccg.search.filter.MECCGFilter
import java.io.Serializable

class MECCGTypeFilter(
    options: List<MECCGTypeOption>,
    notSelectedOption: MECCGTypeOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), MECCGFilter, Serializable {
    override fun filter(card: MECCGCard, setRepository: MECCGSetRepository): Boolean {
        return (selectedOption as MECCGTypeOption).name == card.primary
    }
}