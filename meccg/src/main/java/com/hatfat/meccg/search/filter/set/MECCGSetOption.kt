package com.hatfat.meccg.search.filter.set

import com.hatfat.cards.search.filter.SpinnerOption

data class MECCGSetOption(
    val name: String,
    val code: String
) : SpinnerOption {
    override val displayName: String
        get() = name
}