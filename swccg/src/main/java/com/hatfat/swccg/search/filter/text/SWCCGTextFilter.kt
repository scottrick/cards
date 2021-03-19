package com.hatfat.swccg.search.filter.text

import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.search.filter.SWCCGFilter

class SWCCGTextFilter(
    private val filterText: String,
    private val textFilterModes: Set<SWCCGTextFilterMode>
) : SWCCGFilter {
    override fun filter(card: SWCCGCard): Boolean {
        if (textFilterModes.contains(SWCCGTextFilterMode.TITLE) &&
            (card.front.title?.contains(filterText, true) == true
                    || card.back?.title?.contains(filterText, true) == true)
        ) {

            return true
        }

        if (textFilterModes.contains(SWCCGTextFilterMode.GAMETEXT) &&
            (card.front.gametext?.contains(filterText, true) == true
                    || card.back?.gametext?.contains(filterText, true) == true)
        ) {
            return true
        }

        if (textFilterModes.contains(SWCCGTextFilterMode.LORE) &&
            (card.front.lore?.contains(filterText, true) == true
                    || card.back?.lore?.contains(filterText, true) == true)
        ) {
            return true
        }

        return false
    }
}