package com.hatfat.swccg.search.filter.side

import com.hatfat.cards.search.filter.SpinnerOption

data class SWCCGSideOption(
    val name: String
) : SpinnerOption {
    override val displayName: String
        get() = name
}