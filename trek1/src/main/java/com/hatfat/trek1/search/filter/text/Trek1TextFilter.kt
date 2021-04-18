package com.hatfat.trek1.search.filter.text

import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.search.filter.Trek1Filter

class Trek1TextFilter(
    private val filterText: String,
    private val textFilterModes: Set<Trek1TextFilterMode>
) : Trek1Filter {
    override fun filter(card: Trek1Card): Boolean {

        if (textFilterModes.contains(Trek1TextFilterMode.TITLE) &&
            card.name?.contains(filterText, true) == true
        ) {
            return true
        }

        if (textFilterModes.contains(Trek1TextFilterMode.GAMETEXT)
            && card.text?.contains(filterText, true) == true
        ) {
            return true
        }

        if (textFilterModes.contains(Trek1TextFilterMode.LORE)
            && card.lore?.contains(filterText, true) == true
        ) {
            return true
        }

        return false
    }
}