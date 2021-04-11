package com.hatfat.meccg.search.filter.alignment

import com.hatfat.cards.search.filter.SpinnerOption

data class MECCGAlignmentOption(
    val name: String
) : SpinnerOption {
    override val displayName: String
        get() = name
}
