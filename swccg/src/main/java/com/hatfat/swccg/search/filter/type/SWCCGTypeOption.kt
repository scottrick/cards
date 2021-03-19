package com.hatfat.swccg.search.filter.type

import com.hatfat.cards.search.filter.SpinnerOption

data class SWCCGTypeOption(
    val type: String
) : SpinnerOption {
    override val displayName: String
        get() = type
}
