package com.hatfat.meccg.search.filter.key

import com.hatfat.cards.search.filter.SpinnerOption

data class MECCGKeyOption(
    val name: String
) : SpinnerOption {
    override val displayName: String
        get() = name
}