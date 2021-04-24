package com.hatfat.trek2.search.filter.text

import com.hatfat.trek2.data.Trek2Card
import com.hatfat.trek2.repo.Trek2SetRepository
import com.hatfat.trek2.search.filter.Trek2Filter

class Trek2TextFilter(
    private val filterText: String,
    private val textFilterModes: Set<Trek2TextFilterMode>
) : Trek2Filter {
    override fun filter(card: Trek2Card, setRepository: Trek2SetRepository): Boolean {
        if (textFilterModes.contains(Trek2TextFilterMode.TITLE) &&
            (card.name?.contains(filterText, true) == true)
        ) {
            return true
        }

        if (textFilterModes.contains(Trek2TextFilterMode.TEXT)
            && card.text?.contains(filterText, true) == true
        ) {
            return true
        }

        return false
    }
}