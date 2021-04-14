package com.hatfat.meccg.search.filter.type

import com.hatfat.cards.search.filter.SpinnerOption

data class MECCGTypeOption(
    val name: String
) : SpinnerOption {
    override val displayName: String
        get() = name
}