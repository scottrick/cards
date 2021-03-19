package com.hatfat.swccg.search.filter.set

import com.hatfat.cards.search.filter.SpinnerOption

data class SWCCGSetOption(
    val name: String,
    val id: String
) : SpinnerOption {
    override val displayName: String
        get() = name
}