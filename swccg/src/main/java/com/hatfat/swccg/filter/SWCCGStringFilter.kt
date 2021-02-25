package com.hatfat.swccg.filter

import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.search.SWCCGStringSearchOptions

class SWCCGStringFilter(
    private val filterText: String,
    private val stringSearchOptions: Set<SWCCGStringSearchOptions>
) : SWCCGFilter() {
    override fun filter(card: SWCCGCard): Boolean {
        if (stringSearchOptions.contains(SWCCGStringSearchOptions.TITLE) &&
            card.front.title?.contains(filterText, true) == true
            || card.back?.title?.contains(filterText, true) == true
        ) {
            return true
        }

        if (stringSearchOptions.contains(SWCCGStringSearchOptions.GAMETEXT) &&
            card.front.gametext?.contains(filterText, true) == true
            || card.back?.gametext?.contains(filterText, true) == true
        ) {
            return true
        }

        if (stringSearchOptions.contains(SWCCGStringSearchOptions.LORE) &&
            card.front.lore?.contains(filterText, true) == true
            || card.back?.lore?.contains(filterText, true) == true
        ) {
            return true
        }

        return false
    }
}