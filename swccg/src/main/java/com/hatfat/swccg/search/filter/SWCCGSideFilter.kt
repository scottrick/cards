package com.hatfat.swccg.search.filter

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.swccg.data.SWCCGCard

class SWCCGSideFilter : SpinnerFilter(
    "Side",
    listOf("Any Side", "Light", "Dark"),
    0
), SWCCGFilter {
    override fun filter(card: SWCCGCard): Boolean {
        return options[selectedIndex] == card.side
    }
}