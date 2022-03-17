package com.hatfat.swccg.search.filter.text

import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.repo.SWCCGSetRepository
import com.hatfat.swccg.search.filter.SWCCGFilter

class SWCCGTextFilter(
    private val filterText: String,
    private val textFilterModes: Set<SWCCGTextFilterMode>
) : SWCCGFilter {
    override fun filter(card: SWCCGCard, setRepository: SWCCGSetRepository): Boolean {
        if (textFilterModes.contains(SWCCGTextFilterMode.TITLE_ABBR) &&
            (card.front.title?.contains(filterText, true) == true
                    || card.back?.title?.contains(filterText, true) == true)
        ) {
            return true
        }

        if (textFilterModes.contains(SWCCGTextFilterMode.TITLE_ABBR)) {
            card.abbr?.forEach { abbr ->
                if (abbr.contains(filterText, true)) {
                    return true
                }
            }
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