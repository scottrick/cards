package com.hatfat.meccg.search.filter.text

import com.hatfat.meccg.data.MECCGCard
import com.hatfat.meccg.repo.MECCGSetRepository
import com.hatfat.meccg.search.filter.MECCGFilter

class MECCGTextFilter(
    private val filterText: String,
    private val textFilterModes: Set<MECCGTextFilterMode>
) : MECCGFilter {
    override fun filter(card: MECCGCard, setRepository: MECCGSetRepository): Boolean {
        if (textFilterModes.contains(MECCGTextFilterMode.TITLE) &&
            (card.normalizedTitle?.contains(filterText, true) == true
                    || card.nameEN?.contains(filterText, true) == true)
        ) {
            return true
        }

        return (textFilterModes.contains(MECCGTextFilterMode.TEXT)
                && card.text?.contains(filterText, true) == true)
    }
}